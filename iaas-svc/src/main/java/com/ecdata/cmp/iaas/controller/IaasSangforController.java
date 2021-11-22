package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.SangforSafeClient;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.MinTimeObject;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.SangforRiskPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.SangforRiskResponse;
import com.ecdata.cmp.iaas.entity.sangfor.SangforSecurityRisk;
import com.ecdata.cmp.iaas.service.IaasSangforRiskService;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ZhaoYX
 * @since 2020/4/21 10:43,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sangfor")
@Api(tags = "深信服相关的API")
public class IaasSangforController {
    @Autowired
    IaasSangforRiskService iaasSangforRiskService;
    @Autowired
    private
    SangforSafeClient sangforSafeClient;
    //定时任务调用，不能有权限   有则更新，无则新增
    @PostMapping("/add_batch")
    @ApiOperation(value = "批量新增风险", notes = "批量新增风险")
    public ResponseEntity<BaseResponse> addBatch(@RequestBody List<SangforSecurityRiskVO> riskVOs) {
        BaseResponse baseResponse = new BaseResponse();

        try{
            Sign.setCurrentTenantId(10000L);
            List<SangforSecurityRisk> risks = new ArrayList<>();
            for(SangforSecurityRiskVO riskVO:riskVOs){
                SangforSecurityRisk risk = new SangforSecurityRisk();
                BeanUtils.copyProperties(riskVO,risk);
                QueryWrapper<SangforSecurityRisk> queryWrapper = new QueryWrapper<>();
                // 1.风险业务  2.风险终端  3.风险事件  4.弱密码  5.明文传输  6.漏洞
                LambdaQueryWrapper<SangforSecurityRisk> wrapper = queryWrapper.lambda();

                Optional.ofNullable(risk.getRiskType()).ifPresent(e -> wrapper.eq(SangforSecurityRisk::getRiskType, e));
                Optional.ofNullable(risk.getIp()).ifPresent(e -> wrapper.eq(SangforSecurityRisk::getIp, e));
                Optional.ofNullable(risk.getBranchId()).ifPresent(e -> wrapper.eq(SangforSecurityRisk::getBranchId, e));

                Optional.ofNullable(risk.getRiskType()).filter(e -> e <= 3).ifPresent(e -> {
                    // 1.风险业务  2.风险终端  3.风险事件
                    // 风险业务/终端:groupId、ip、type、branchId
                    // 安全事件:branchId、groupId、type、ip、ruleId、recordDate
                    log.info("1.riskType={}", e);
                    Optional.ofNullable(risk.getGroupId()).ifPresent(t -> wrapper.eq(SangforSecurityRisk::getGroupId, t));
                    Optional.ofNullable(risk.getType()).ifPresent(t -> wrapper.eq(SangforSecurityRisk::getType, t));

                    if (e == 3) {
                        // 3.风险事件
                        // 安全事件:branchId、groupId、type、ip、ruleId、recordDate
                        log.info("2.riskType={}", e);
                        Optional.ofNullable(risk.getRuleId()).ifPresent(t -> wrapper.eq(SangforSecurityRisk::getRuleId, t));
                        Optional.ofNullable(risk.getRecordDate()).ifPresent(t -> wrapper.eq(SangforSecurityRisk::getRecordDate, t));
                    }
                });

                Optional.ofNullable(risk.getRiskType()).filter(e -> e > 3).ifPresent(e -> {
                    // 4.弱密码  5.明文传输  6.漏洞
                    // 弱密码/漏洞信息/明文传输:ip、branchId、eventKey
                    log.info("3.riskType={}", e);
                    Optional.ofNullable(risk.getEventKey()).ifPresent(t -> wrapper.eq(SangforSecurityRisk::getEventKey, t));
                });

                List<SangforSecurityRisk> exists = iaasSangforRiskService.list(wrapper);
                SangforSecurityRisk exist = null;
                if(CollectionUtils.isNotEmpty(exists))
                    exist = exists.get(0);
                boolean update = false;
                boolean insert = false;
                if(null != exist){
                    update=iaasSangforRiskService.update(risk,wrapper);
                    log.info("risk-addbatch-update "+exist);
                }else{
                    risk.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    insert=iaasSangforRiskService.save(risk);
                    if(risk.getDealStatus()==0)
                        risks.add(risk);
                    log.info("risk-addbatch-insert "+risk);
                }
                if(update||insert){
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("添加风险成功");
                }else{
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("添加风险失败");
                }
            }
            iaasSangforRiskService.addBatchToNotify(risks);
        }catch (Exception e){
            log.debug("forgetPassword异常"+e);
            e.printStackTrace();
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        }finally {
            Sign.removeCurrentTenantId();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }


    //单条update    页面处置
    @PostMapping("/update")
    @ApiOperation(value = "风险更新", notes = "风险更新")
    public ResponseEntity<BaseResponse> update(@RequestBody SangforSecurityRiskVO riskVO) {//dealComment
        BaseResponse baseResponse = new BaseResponse();
        QueryWrapper<SangforSecurityRisk> queryWrapper = new QueryWrapper<>();
        SangforSecurityRisk risk = new SangforSecurityRisk();
        if(riskVO.getId()!=null){
            risk = iaasSangforRiskService.getById(riskVO.getId());
            risk.setDealComment(riskVO.getDealComment());
            log.info("找到对应风险 "+risk);
            BeanUtils.copyProperties(risk,riskVO);
            //调用深信服接口
            boolean handled = sangforSafeClient.getRiskBusiness(riskVO);
            log.info("updatedToSangforClient "+handled);
            if(handled){
                try{
                    Sign.setCurrentTenantId(10000L);
                    if(risk.getId()!=null) {
                        log.info("beforeUpdate "+risk);
                        List<SysNotificationReceiverVO> voList = iaasSangforRiskService.fetchReceiveIdByRisk(risk.getId());
                        risk.setDealStatus(1);
                        log.info("updated "+risk);
                        if(CollectionUtils.isNotEmpty(voList)){
                            for(SysNotificationReceiverVO vo:voList){
                                Long receiverId= vo.getId();
                                iaasSangforRiskService.updateToNotify(riskVO,receiverId);//
                            }
                        }
                    }
                    else{
                        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                        baseResponse.setMessage("更新风险失败，当前风险不存在");
                    }
                }catch (Exception e){
                    log.debug("forgetPassword异常"+e);
                    e.printStackTrace();
                    return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
                }finally {
                    Sign.removeCurrentTenantId();
                }
                iaasSangforRiskService.updateById(risk);
                log.info("updated database "+risk);
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("更新风险成功");
            }else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("更新风险失败");
            }
        }  else{
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("未找到对应风险");
            log.info("未找到对应风险 "+risk);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    //单条显示   详情
    @GetMapping("/find/{id}")
    @ApiOperation(value = "查询详情", notes = "查询详情")
    @ApiImplicitParam(name = "id", value = "风险id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> find(@PathVariable(name = "id") Long id) {
        SangforRiskResponse response = new SangforRiskResponse();
        SangforSecurityRisk risk = iaasSangforRiskService.getOne(
                new QueryWrapper<SangforSecurityRisk>().eq("id",id));
        SangforSecurityRiskVO vo = new SangforSecurityRiskVO();
        BeanUtils.copyProperties(risk,vo);
        if(risk!=null){
            response.setData(vo);
            response.setMessage("成功找到该对象");
        }else{
            response.setMessage("没有找到该对象");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //分页查询
    @GetMapping("/page")
    @ApiOperation(value = "分页查询风险信息", notes = "分页查询风险信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "5"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "告警等级", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "riskType", value = "风险类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "reliability", value = "威胁性等级", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "priority", value = "失陷确定性", paramType = "query", dataType = "string"),
    })
    public ResponseEntity<SangforRiskPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                      @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                      @RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String level,
                                                        @RequestParam(required = false) String riskType,
                                                        @RequestParam(required = false) String reliability,
                                                        @RequestParam(required = false) String priority) {

        QueryWrapper<SangforSecurityRisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SangforSecurityRisk::getDealStatus,0);

        if (StringUtils.isNotEmpty(level)) {
            queryWrapper.lambda().and(obj->obj.eq(SangforSecurityRisk::getLevel, level));
        }
        if (StringUtils.isNotEmpty(riskType)) {
            queryWrapper.lambda().and(obj->obj.eq(SangforSecurityRisk::getRiskType, riskType));
        }
        if (StringUtils.isNotEmpty(reliability)) {
            queryWrapper.lambda().and(obj->obj.eq(SangforSecurityRisk::getReliability, reliability));
        }
        if (StringUtils.isNotEmpty(priority)) {
            queryWrapper.lambda().and(obj->obj.eq(SangforSecurityRisk::getPriority, priority));
        }
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda()
                    .like(SangforSecurityRisk::getIp,keyword)
                    .or().like(SangforSecurityRisk::getBranchName,keyword)
                    .or().like(SangforSecurityRisk::getGroupName,keyword)
                    .or().like(SangforSecurityRisk::getRecordDate,keyword);
//                    .like(SangforSecurityRisk::getRiskType,keyword)
//                    .or().like(SangforSecurityRisk::getLastTime,keyword)
//                    .or()

//                    .or().like(SangforSecurityRisk::getDealStatus,keyword)
//                    .or().like(SangforSecurityRisk::getDealTime,keyword)
//                    .or().like(SangforSecurityRisk::getEventKey,keyword)

//                    .or().like(SangforSecurityRisk::getType,keyword)
//                    .or().like(SangforSecurityRisk::getSeverityLevel,keyword)
//                    .or().like(SangforSecurityRisk::getReliability,keyword)

//                    .or().like(SangforSecurityRisk::getMetaData,keyword);
//                    .or().like(SangforSecurityRisk::getLevel,keyword)
//                    .or().like(SangforSecurityRisk::getPriority,keyword);
        }
        log.info("keyword "+keyword);
        log.info("level "+level);
        log.info("riskType "+riskType);
        log.info("reliability "+reliability);
        log.info("priority "+priority);
        queryWrapper.lambda().orderByDesc(SangforSecurityRisk::getRecordDate);
        Page<SangforSecurityRisk> page = new Page<>(pageNo, pageSize);
        IPage<SangforSecurityRisk> result = iaasSangforRiskService.page(page, queryWrapper);
        List<SangforSecurityRiskVO> voList = new ArrayList<>();
        List<SangforSecurityRisk> list = result.getRecords();
        log.info("riskList "+list);
        for(SangforSecurityRisk risk:list){
            SangforSecurityRiskVO vo = new SangforSecurityRiskVO();
            BeanUtils.copyProperties(risk,vo);
            voList.add(vo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new SangforRiskPageResponse(new PageVO<>(result,voList)));
    }

    //分页查询
    @GetMapping("/min_unhandled")
    @ApiOperation(value = "最小未处理时间", notes = "最小未处理时间")
    public MinTimeObject minUnhandled(){
        MinTimeObject minTime = iaasSangforRiskService.getMinTime();
        return minTime;
    }



}

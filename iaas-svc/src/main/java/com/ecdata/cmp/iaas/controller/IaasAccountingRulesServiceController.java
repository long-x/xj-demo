package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.iaas.entity.IaasAccountingRules;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingRulesVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasAccountingRulesServicePageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasAccountingRulesServiceResponse;
import com.ecdata.cmp.iaas.service.IaasAccountingRulesService;
import com.ecdata.cmp.user.dto.response.chargeable.ChargeableMapResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 11:04
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/accounting_rules")
@Api(tags = "计量计费")
public class IaasAccountingRulesServiceController {

    @Autowired
    private IaasAccountingRulesService accountingRulesService;


    @PostMapping("/add")
    @ApiOperation(value = "新增计费模型", notes = "新增计费模型")
    public ResponseEntity<BaseResponse> addBatch(@RequestBody IaasAccountingRulesVO vo) {
            BaseResponse baseResponse = new BaseResponse();
            if(accountingRulesService.save(vo)>=1){
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("添加计费成功");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }else{
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("该时间段存在计费设置,请勿重复添加");
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询计费", notes = "根据id查询计费")
    @ApiImplicitParam(name = "id", value = "计费模型id", required = true, paramType = "path")
    public ResponseEntity<IaasAccountingRulesServiceResponse> getById(@PathVariable(name = "id") Long id) {
        IaasAccountingRules iaasAccountingRules = accountingRulesService.getById(id);
        IaasAccountingRulesVO iaasAccountingRulesVO = new IaasAccountingRulesVO();
        BeanUtils.copyProperties(iaasAccountingRules, iaasAccountingRulesVO);

        return ResponseEntity.status(HttpStatus.OK).body(new IaasAccountingRulesServiceResponse(iaasAccountingRulesVO));
    }


    @GetMapping("/remove")
    @ApiOperation(value = "根据id删除记录",notes = "根据id删除记录")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "long")
    public ResponseEntity<BaseResponse> getInfo(@RequestParam(required = false) Long id) {
        BaseResponse baseResponse = new BaseResponse();

        if (id != null&&accountingRulesService.getById(id)!=null) {
            accountingRulesService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除计费成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败,不存在该记录");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }




    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除计费模型 ", notes = "批量删除计费模型 (逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除计费模型  ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        //ids参数为空
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //删除
            List<String> idList = Arrays.asList(idArray);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            for(String id :idList){
                accountingRulesService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }



    @PutMapping("/update")
    @ApiOperation(value = "更新计费模型 ", notes = "更新计费模型 ")
    public ResponseEntity<BaseResponse> update(@RequestBody IaasAccountingRulesVO iaasAccountingRulesVO){
        BaseResponse baseResponse = new BaseResponse();
        IaasAccountingRules iaasAccountingRules = new IaasAccountingRules();
        BeanUtils.copyProperties(iaasAccountingRulesVO, iaasAccountingRules);
        //响应失败，缺少主键
        if (iaasAccountingRules.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        iaasAccountingRules.setUpdateUser(Sign.getUserId());
        iaasAccountingRules.setUpdateTime(DateUtil.getNow());

        if (accountingRulesService.updateById(iaasAccountingRules)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }



    @GetMapping("/page")
    @ApiOperation(value = "分页查看计费信息", notes = "分页查看计费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "effectiveDate", value = "起始时间(yyyy-MM-dd)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "expirationDate", value = "结束时间(yyyy-MM-dd)", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasAccountingRulesServicePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                       @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                       @RequestParam(required = false) String status,
                                                                       @RequestParam(required = false) String effectiveDate,
                                                                       @RequestParam(required = false) String expirationDate
    ) {
        Page<IaasAccountingRulesVO> page = new Page<>(pageNo, pageSize);
        //封装查询条件map
        Map<String, Object> params = new HashMap<>();
        if (status != null) {
            params.put("status", status);
        }
        if (effectiveDate != null) {
            params.put("effectiveDate", effectiveDate);
        }
        if (expirationDate != null) {
            params.put("expirationDate", expirationDate);
        }

        IPage<IaasAccountingRulesVO> result = accountingRulesService.qrList(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasAccountingRulesServicePageResponse(new PageVO<>(result)));
    }









    }

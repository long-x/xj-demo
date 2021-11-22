package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import com.ecdata.cmp.user.dto.response.chargeable.ChargeableMapResponse;
import com.ecdata.cmp.user.dto.response.chargeable.ChargeableResponse;
import com.ecdata.cmp.user.entity.SysChargeable;
import com.ecdata.cmp.user.service.ISysChargeableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author ZhaoYX
 * @since 2019/11/27 13:08,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sys_chargeable")
@Api(tags = "计费模型的API")
public class SysChargeableController {

    @Autowired
    ISysChargeableService chargeableService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询计费模型", notes = "根据id查询计费模型")
    @ApiImplicitParam(name = "id", value = "计费模型id", required = true, paramType = "path")
    public ResponseEntity<ChargeableResponse> getById(@PathVariable(name = "id") Long id) {
        SysChargeable sysChargeable = chargeableService.getById(id);
        SysChargeableVO sysChargeableVO = new SysChargeableVO();
        BeanUtils.copyProperties(sysChargeable, sysChargeableVO);

        return ResponseEntity.status(HttpStatus.OK).body(new ChargeableResponse(sysChargeableVO));
    }
    
    @PostMapping("/add")
    @ApiOperation(value = "新增计费模型", notes = "新增计费模型")
    public ResponseEntity<BaseResponse> add(@RequestBody SysChargeableVO chargeableVO){
        BaseResponse baseResponse = new BaseResponse();
        SysChargeable chargeable = new SysChargeable();
        BeanUtils.copyProperties(chargeableVO, chargeable);

        chargeable.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow())
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
        if (chargeableService.save(chargeable)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            log.info("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询计费模型", notes = "分页查询计费模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ChargeableMapResponse> page(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) String createTimeStart,
            @RequestParam(required = false) String createTimeEnd,
            @RequestParam(required = false) String keyword) {
        QueryWrapper<SysChargeable> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(createTimeStart)) {
            queryWrapper.lambda().ge(SysChargeable::getCreateTime, createTimeStart);
        }

        if (StringUtils.isNotEmpty(createTimeEnd)) {
            queryWrapper.lambda().le(SysChargeable::getCreateTime, createTimeEnd);
        }
        if (StringUtils.isNotEmpty(keyword)) {
//            queryWrapper.lambda().like(SysChargeable::getMessage, keyword);
        }
        Page<SysChargeableVO> page = new Page<>(pageNo, pageSize);
        /*
        需要自定义IPage
         */
        IPage<SysChargeableVO> result = chargeableService.qryChargeInfo(page, queryWrapper);
        List<SysChargeableVO> chargeList = result.getRecords();
        
//        List<SysChargeableVO> sysChVOList = new ArrayList<>();
//        if (chargeList != null && chargeList.size() > 0) {
//            for (SysChargeableVO sysno : chargeList) {
//                SysChargeableVO sysnoVO = new SysChargeableVO();
//                BeanUtils.copyProperties(sysno, sysnoVO);
//                sysChVOList.add(sysnoVO);
//            }
//        }
        log.info(String.valueOf(chargeList));
        Map<String,Object> selection = this.selection(result,chargeList);
        log.info(String.valueOf(selection));

        return ResponseEntity.status(HttpStatus.OK).
                body(new ChargeableMapResponse(selection));
    }

    private Map<String,Object> selection(IPage<SysChargeableVO> result,List<SysChargeableVO> sysChVOList) {

        Map<String,Object> map = new LinkedHashMap<>();

        List<SysChargeableVO> clouds = chargeableService.getIaasSelection();
        List<Map<String,Object>> pools = new ArrayList<>();
        List<Map<String,Object>> providers = new ArrayList<>();

        for(SysChargeableVO vo:clouds){
            Map<String,Object> pool = new LinkedHashMap<>();
            pool.put("poolId",vo.getPoolId());
            pool.put("poolName",vo.getPoolName());
            pools.add(pool);
            Map<String,Object> provider = new LinkedHashMap<>();
            provider.put("proId",vo.getProviderId());
            provider.put("proName",vo.getProviderName());
            providers.add(provider);
        }

        map.put("pools",pools);
        map.put("providers",providers);
        map.put("pageDate",new PageVO<>(result, sysChVOList));
        //平台
        return  map;

    }


    @PutMapping("/update")
        @ApiOperation(value = "更新计费模型 ", notes = "更新计费模型 ")
        public ResponseEntity<BaseResponse> update(@RequestBody SysChargeableVO sysChargeableVO) throws Exception {
            BaseResponse baseResponse = new BaseResponse();
            SysChargeable sysChargeable = new SysChargeable();
            BeanUtils.copyProperties(sysChargeableVO, sysChargeable);
            //响应失败，缺少主键
            if (sysChargeable.getId() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
            }
            //address+type校验:address存ip地址，静态模版已最新的为准。
//            if (checkProByTypeAdress(sysChargeableVO, baseResponse, sysChargeable))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);

            //update user+time
            sysChargeable.setUpdateUser(Sign.getUserId());
            sysChargeable.setUpdateTime(DateUtil.getNow());

            if (chargeableService.updateById(sysChargeable)) {
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("更新成功");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("更新失败");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        }


        /**
         * 删除（逻辑删除）,0 false 删除
         *
         * @param id
         * @return BaseResponse
         */
        @PutMapping("/remove")
        @ApiOperation(value = "删除计费模型 ", notes = "删除计费模型(逻辑删除)")
        public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
            log.info("删除计费模型  user id：{}", id);
            BaseResponse baseResponse = new BaseResponse();
            if (id != null&&chargeableService.getById(id)!=null) {
                //逻辑删除
                if(chargeableService.removeById(id)){
                    chargeableService.modifyUpdateRecord(id, Sign.getUserId());
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("删除成功");
                    return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
                }else {
                    baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                    baseResponse.setMessage("删除失败");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
                }
            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("删除失败,不存在该记录");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
        }


        /**
         * 批量删除
         *
         * @param ids
         * @return BaseResponse
         */
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
                this.chargeableService.removeByIds(idList);
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("删除成功");
                for(String id :idList){
                    chargeableService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                }
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }
        }
        
    

}

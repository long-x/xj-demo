package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.GroupAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.dto.response.SysBusinessPoolListResponse;
import com.ecdata.cmp.user.dto.response.SysBusinessPoolPageResponse;
import com.ecdata.cmp.user.dto.response.SysBusinessPoolResponse;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import com.ecdata.cmp.user.entity.SysBusinessGroupResourcePool;
import com.ecdata.cmp.user.service.ISysBusinessGroupResourcePoolService;
import com.ecdata.cmp.user.service.ISysBusinessGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ???xuj
 * @date ???Created in 2019/11/21 10:21
 * @modified By???
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sys_business_pool")
@Api(tags = "???????????????????????????")
public class SysBusinessPoolController {


    @Autowired
    private ISysBusinessGroupResourcePoolService poolService;
    @Autowired
    private ISysBusinessGroupService iSysBusinessGroupService;

    @PostMapping("/add")
    @ApiOperation(value = "??????", notes = "??????")
    public ResponseEntity<BaseResponse> add(@RequestBody SysBusinessPoolVO sysBusinessPoolVO) {
        SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
        BeanUtils.copyProperties(sysBusinessPoolVO, pool);
        pool.setId(SnowFlakeIdGenerator.getInstance().nextId());
        pool.setCreateTime(DateUtil.getNow());
        BaseResponse baseResponse = new BaseResponse();
        if (poolService.save(pool)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/delete_by_id")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    public ResponseEntity<BaseResponse> deleteById(@RequestParam Long id) {
        log.info("???????????? user id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (poolService.deleteById(id) > 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/delete_by_id_batch")
    @ApiOperation(value = "??????id???????????????????????????", notes = "??????id???????????????????????????")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("???????????? ids???{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.PARAM_TYPE_ERROR.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            List<String> strings = Arrays.asList(idArray);
            for (String id : strings) {
                this.poolService.deleteById(Long.parseLong(id));
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }


    @PostMapping("/add_batch")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> addBatch(@RequestBody List<SysBusinessPoolVO> poolVOList) {
        BaseResponse baseResponse = new BaseResponse();
        if (poolService.save(poolVOList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/remove")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> remove(@RequestBody List<SysBusinessPoolVO> poolVOList) {
        BaseResponse baseResponse = new BaseResponse();
        if (poolService.deleteByPoolId(poolVOList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/update")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> update(@RequestBody List<SysBusinessPoolVO> poolVOList) {

        BaseResponse baseResponse = new BaseResponse();
        if (poolService.updateByIds(poolVOList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PostMapping("/add_group_and_pool")
    @ApiOperation(value = "??????pool_id???business_group_name??????????????????", notes = "??????pool_id???business_group_name??????????????????")
    public ResponseEntity<BaseResponse> add(@RequestBody GroupAndPoolVO groupAndPoolVO) {
        //????????????
        SysBusinessGroup group = new SysBusinessGroup();
        BeanUtils.copyProperties(groupAndPoolVO, group);
        long groupId = SnowFlakeIdGenerator.getInstance().nextId();
        group.setId(groupId);
        group.setCreateTime(DateUtil.getNow());
        group.setUpdateTime(DateUtil.getNow());
        //???????????????
        SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
        BeanUtils.copyProperties(groupAndPoolVO, pool);
        pool.setId(SnowFlakeIdGenerator.getInstance().nextId());
        pool.setCreateTime(DateUtil.getNow());
        pool.setBusinessGroupId(groupId);
        BaseResponse baseResponse = new BaseResponse();

        if (iSysBusinessGroupService.save(group) && poolService.save(pool)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update_correlation_user")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> updateCorrelation(@RequestBody SysBusinessMemberAndUserAndPoolVO memberAndUserAndPoolVO) {
        BaseResponse baseResponse = new BaseResponse();
        if (poolService.updateCorrelationPool(memberAndUserAndPoolVO)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "??????id??????????????????????????????", notes = "??????id??????????????????????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<SysBusinessPoolResponse> getById(@PathVariable(name = "id") Long id) {
        SysBusinessPoolResponse memberResponse = new SysBusinessPoolResponse();
        SysBusinessGroupResourcePool pool = poolService.getById(id);
        if (pool == null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
        }
        SysBusinessPoolVO poolVO = new SysBusinessPoolVO();
        BeanUtils.copyProperties(pool, poolVO);
        memberResponse.setData(poolVO);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }


    @GetMapping("/list")
    @ApiOperation(value = "??????????????????????????????????????????", notes = "??????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "??????(1:iaas;2:paas;)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "businessGroupId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "poolId", value = "?????????id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<SysBusinessPoolListResponse> list(@RequestParam(required = false) Integer type,
                                                            @RequestParam(required = false) Long businessGroupId,
                                                            @RequestParam(required = false) Long poolId) {
        List<SysBusinessPoolVO> poolVOList = new ArrayList<>();
        QueryWrapper<SysBusinessGroupResourcePool> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.lambda().eq(SysBusinessGroupResourcePool::getType, type);
        }
        if (businessGroupId != null) {
            queryWrapper.lambda().eq(SysBusinessGroupResourcePool::getBusinessGroupId, businessGroupId);
        }
        if (poolId != null) {
            queryWrapper.lambda().eq(SysBusinessGroupResourcePool::getPoolId, poolId);
        }

        List<SysBusinessGroupResourcePool> poolList = poolService.list(queryWrapper);
        if (poolList != null && poolList.size() > 0) {
            for (SysBusinessGroupResourcePool member : poolList) {
                SysBusinessPoolVO memberVO = new SysBusinessPoolVO();
                BeanUtils.copyProperties(member, memberVO);
                poolVOList.add(memberVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessPoolListResponse(poolVOList));
    }


    @GetMapping("/page")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysBusinessPoolPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                            @RequestParam(required = false) String keyword) {
        Page<SysBusinessPoolVO> page = new Page<>(pageNo, pageSize);
        IPage<SysBusinessPoolVO> result = poolService.qrySysBusinessPoolInfo(page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessPoolPageResponse(new PageVO<>(result)));
    }


    //???????????????2019-11-22
//    @PutMapping("/remove")
//    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
//    public ResponseEntity<BaseResponse> remove(@RequestParam Long pooId) {
//        log.info("?????????????????? id ???{}", pooId);
//
//        BaseResponse baseResponse = new BaseResponse();
//        List<String> groupByPoolId = sysBusinessPoolService.getGroupByPoolId(pooId);
//        log.info("????????????list  ???{}", groupByPoolId.toString());
//        if (iSysBusinessGroupService.removeByIds(groupByPoolId)) {
//            sysBusinessPoolService.deleteByPoolId(pooId);
//            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
//            baseResponse.setMessage("????????????");
//            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
//        }
//
//        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
//        baseResponse.setMessage("????????????");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
//    }


}

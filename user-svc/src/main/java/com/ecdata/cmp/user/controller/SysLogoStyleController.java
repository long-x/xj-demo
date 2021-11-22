package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysLogoStyleVO;
import com.ecdata.cmp.user.dto.response.SysLogoStyleResponse;
import com.ecdata.cmp.user.entity.SysLogoStyle;
import com.ecdata.cmp.user.service.ISysLogoStyleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @title: SysLogoStyle Controller
 * @Author: shig
 * @description: 系统商标样式 控制层： add，update，info
 * @Date: 2019/11/22 2:18 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sysLogoStyle")
@Api(tags = "🍎系统商标样式相关的API")
public class SysLogoStyleController {

    /**
     * SysLogoStyle Service
     */
    @Autowired
    private ISysLogoStyleService sysLogoStyleService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增统系统商标样式", notes = "新增统系统商标样式")
    public ResponseEntity<BaseResponse> add(@RequestBody SysLogoStyleVO sysLogoStyleVO) throws Exception {
        SysLogoStyle sysLogoStyle = new SysLogoStyle();
        BeanUtils.copyProperties(sysLogoStyleVO, sysLogoStyle);
        sysLogoStyle.setCreateUser(Sign.getUserId());
        sysLogoStyle.setCreateTime(DateUtil.getNow());
        sysLogoStyle.setUpdateTime(DateUtil.getNow());
        sysLogoStyle.setId(SnowFlakeIdGenerator.getInstance().nextId());
        BaseResponse baseResponse = new BaseResponse();
        if (sysLogoStyleService.save(sysLogoStyle)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/update")
    @ApiOperation(value = "修改系统商标样式", notes = "修改系统商标样式")
    public ResponseEntity<BaseResponse> update(@RequestBody SysLogoStyleVO sysLogoStyleVO) throws Exception {
        SysLogoStyle sysLogoStyle = new SysLogoStyle();
        BeanUtils.copyProperties(sysLogoStyleVO, sysLogoStyle);
        //响应失败，缺少主键
        if (sysLogoStyle.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        //update user+time
        sysLogoStyle.setUpdateUser(Sign.getUserId());
        sysLogoStyle.setUpdateTime(DateUtil.getNow());

        //响应
        BaseResponse baseResponse = null;
        if (sysLogoStyleService.updateById(sysLogoStyle)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取系统商标样式信息", notes = "获取系统商标样式信息")
    @ApiImplicitParam(name = "id", value = "系统商标样式id", paramType = "query", dataType = "long")
    public ResponseEntity<SysLogoStyleResponse> getInfo(@RequestParam(required = false) Long id) {
        SysLogoStyleResponse logoStyleResponse = new SysLogoStyleResponse();
        SysLogoStyleVO logoStyleVO = new SysLogoStyleVO();
        if (logoStyleVO == null) {
            id = Sign.getUserId();
        }
        SysLogoStyle sysLogoStyle = sysLogoStyleService.getById(id);
        if (sysLogoStyle == null) {
            return ResponseEntity.status(HttpStatus.OK).body(logoStyleResponse);
        }
        BeanUtils.copyProperties(sysLogoStyle, logoStyleVO);
        logoStyleResponse.setData(logoStyleVO);
        return ResponseEntity.status(HttpStatus.OK).body(logoStyleResponse);
    }


    @GetMapping("/getLastTimeSysLogoStyle")
    @ApiOperation(value = "获取统商标样式信息", notes = "获取最新统商标样式信息")
    public ResponseEntity<SysLogoStyleResponse> getLastTimeSysLogoStyle() throws Exception {
        SysLogoStyleResponse logoStyleResponse = new SysLogoStyleResponse();
        SysLogoStyleVO sysLicenseVO = new SysLogoStyleVO();
        SysLogoStyle sysLogoStyle = sysLogoStyleService.getLastTimeSysLogoStyle();
        BeanUtils.copyProperties(sysLogoStyle, sysLicenseVO);
        logoStyleResponse.setData(sysLicenseVO);
        return ResponseEntity.status(HttpStatus.OK).body(logoStyleResponse);
    }

}
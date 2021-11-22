package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.service.OnOffMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/1 16:35
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/on_off_machine_baremetal")
@Api(tags = "华为模块开关机接口")
public class OnOffMachineBaremetalController {

    @Autowired
    private OnOffMachineService service;


    @GetMapping("/on_off_machine")
    @ApiOperation(value = "开机/关机虚拟机", notes = "开机/关机虚拟机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "vmKey", value = "虚拟机id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "开/关机", paramType = "query", dataType = "string")

    })
    public ResponseEntity<BaseResponse> onOffMachine(@RequestParam(required = false) String projectId,
                                             @RequestParam(required = false) String vmKey,
                                             @RequestParam(required = false) String type) {

        boolean result = service.closeVm(projectId, vmKey, type);
        BaseResponse baseResponse = new BaseResponse();
        if (result) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("开/关虚拟机成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("开/关虚拟机成功");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @GetMapping("/on_off_baremetal")
    @ApiOperation(value = "开机/关机裸金属", notes = "开机/关机裸金属")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "vmKey", value = "虚拟机id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "开/关机", paramType = "query", dataType = "string")

    })
    public ResponseEntity<BaseResponse> onOffBaremetal(@RequestParam(required = false) String projectId,
                                             @RequestParam(required = false) String vmKey,
                                             @RequestParam(required = false) String type) {

        boolean result = service.closeBaremetal(projectId, vmKey, type);
        BaseResponse baseResponse = new BaseResponse();
        if (result) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("开/关机裸金属");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("开/关机裸金属");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


}

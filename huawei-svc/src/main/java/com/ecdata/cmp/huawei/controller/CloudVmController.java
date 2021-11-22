package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.huawei.service.CloudVmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/11 10:27
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/cloud_vm")
@Api(tags = "创建虚拟机接口")
public class CloudVmController {

    @Autowired
    private CloudVmService cloudVmService;


    @PostMapping("/create_vm")
    @ApiOperation(value = "创建虚拟机", notes = "创建虚拟机")
    public ResponseEntity<BaseResponse> add(@RequestBody CloudVmVO cloudVmVO) {
        BaseResponse baseResponse = new BaseResponse();
//        if (cloudVmService.createVm(cloudVmVO)) {
//            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
//            baseResponse.setMessage("创建成功");
//            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
//        } else {
//            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
//            baseResponse.setMessage("创建失败");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
//        }
        log.info("调用一次创建虚拟机接口",cloudVmVO.getName());
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("创建成功");
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }


}

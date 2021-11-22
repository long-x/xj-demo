package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/cloud_vm")
public interface CloudVmClient {

    @PostMapping(path = "/create_vm")
    @ApiOperation(value = "创建虚拟机", notes = "创建虚拟机")
    ResponseEntity<BaseResponse> add(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                     @RequestBody CloudVmVO cloudVmVO)throws Exception;
}

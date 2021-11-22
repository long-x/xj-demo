package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineListResponse;
import com.ecdata.cmp.huawei.dto.response.VmFlavorsResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/virtualMachine")
public interface VDCVirtualMachineClient {

    @PutMapping(path = "/get_virtual_machine_list")
    @ApiOperation(value = "查询虚拟机列表", notes = "查询虚拟机列表")
    VDCVirtualMachineListResponse getVirtualMachineList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                        @RequestBody RequestVO requestVO) throws IOException;

    @PutMapping(path = "/getVmFlavorsList")
    @ApiOperation(value = "查询云服务器规格详情列表", notes = "查询云服务器规格详情列表")
    VmFlavorsResponse getVmFlavorsList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                            @RequestBody RequestVO requestVO) throws IOException;
}

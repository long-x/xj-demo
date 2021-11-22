package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.HostVolumesVOListResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/hostvolumes")
public interface HostVolumesClient {

    @PutMapping(path = "/get_host_volumes")
    @ApiOperation(value = "虚拟机磁盘信息", notes = "虚拟机磁盘信息")
    HostVolumesVOListResponse getHostVolumesList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                 @RequestBody RequestVO requestVO) throws IOException;
}

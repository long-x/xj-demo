package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.PhysicalHostListResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/physicalhost")
public interface PhysicalHostClient {

    @PutMapping(path = "/get_physical_host_list")
    @ApiOperation(value = "宿主机使用率(运营面Token)", notes = "宿主机使用率(运营面Token)")
    PhysicalHostListResponse getPhysicalhostList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                 @RequestBody RequestVO requestVO) throws IOException;
}

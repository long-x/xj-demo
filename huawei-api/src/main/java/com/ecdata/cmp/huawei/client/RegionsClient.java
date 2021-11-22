package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.RegionEntityResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/regions")
public interface RegionsClient {

    @GetMapping(path = "/get_region_entity")
    RegionEntityResponse getRegoinEntity(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                         @RequestBody TokenDTO tokenInfos) throws IOException;
}

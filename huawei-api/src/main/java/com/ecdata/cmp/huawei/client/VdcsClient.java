package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.VdcsListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/vdcs")
public interface VdcsClient {

    @PostMapping(path = "/get_vdcs_list")
    VdcsListResponse getTokenInfoByUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                        @RequestBody TokenDTO tokenInfos) throws IOException;
}

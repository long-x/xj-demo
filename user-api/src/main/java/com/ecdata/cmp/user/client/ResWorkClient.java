package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.response.RoleListResponse;
import com.ecdata.cmp.user.dto.response.VdcsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = UserConstant.SERVICE_NAME, path = "/v1/res_workorder")
public interface ResWorkClient {
    @GetMapping(path = "/get_vdcListForMore")
    VdcsListResponse listVdcs(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token);
}

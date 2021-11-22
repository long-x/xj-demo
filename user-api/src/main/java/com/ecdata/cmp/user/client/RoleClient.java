package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.response.RoleListResponse;
import com.ecdata.cmp.user.dto.response.UserListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2019-11-20
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/role")
public interface RoleClient {

    @GetMapping(path = "/list_admin")
    RoleListResponse listAdmin(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token);

    @GetMapping(path = "/getITDirectors")
    UserListResponse getITDirectors(@RequestParam(name = "roleId", required = false) Long roleId);

}

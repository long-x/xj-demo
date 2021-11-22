package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.api.BooleanResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.response.RightResponse;
import com.ecdata.cmp.user.dto.response.UserListResponse;
import com.ecdata.cmp.user.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2019-11-20
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/user")
public interface UserClient {

    @GetMapping(path = "/{id}")
    UserResponse getById(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                         @PathVariable(name = "id") Long id);

    @GetMapping(path = "/query/{id}")
    UserResponse queryById(@PathVariable(name = "id") Long id);

    @GetMapping(path = "/list")
    UserListResponse list(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token);


    @GetMapping(path = "/is_admin")
    BooleanResponse isAdmin(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                            @RequestParam(name = "userId") Long userId);


    @GetMapping(path = "/right")
    RightResponse getUserRight(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                               @RequestParam(name = "userId", required = false) Long userId);

}

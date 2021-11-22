package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.response.PythonListResponse;
import com.ecdata.cmp.user.dto.response.PythonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2019-11-20
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/v1/python")
public interface SysPythonClient {

    @GetMapping(path = "/{id}")
    PythonResponse getById(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                           @PathVariable(name = "id") Long id);

    @GetMapping(path = "/list")
    PythonListResponse list(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                            @RequestParam(name = "type", required = false) Integer type);

}

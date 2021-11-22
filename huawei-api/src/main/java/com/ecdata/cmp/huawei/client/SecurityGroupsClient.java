package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.SecurityGroupsListResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/securityGroups")
public interface SecurityGroupsClient {
    @GetMapping("/get_security_groups_list")
    @ApiOperation(value = "查询安全组", notes = "查询安全组")
    @ApiImplicitParam(name = "tokenId", value = "token", paramType = "query", dataType = "String")
    SecurityGroupsListResponse getSecurityGroups(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                 @RequestParam(name="tokenId", required = true) String tokenId);
}

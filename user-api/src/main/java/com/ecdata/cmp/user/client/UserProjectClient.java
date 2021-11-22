package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.api.IntegerResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.dto.response.ProjectListResponse;
import com.ecdata.cmp.user.dto.response.ProjectResponse;
import com.ecdata.cmp.user.UserConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2019-09-25
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/project")
public interface UserProjectClient {

    @GetMapping(path = "/name/{projectName}/cluster/{clusterId}")
    ProjectResponse getByNameAndClusterId(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                          @PathVariable(name = "projectName") String projectName,
                                          @PathVariable(name = "clusterId") Long clusterId);

    @GetMapping(path = "/list")
    ProjectListResponse list(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                             @RequestParam(name = "clusterId", required = false) Long clusterId);

    @GetMapping(path = "/list/size")
    IntegerResponse listSize(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                             @RequestParam(name = "clusterId", required = false) Long clusterId);
}

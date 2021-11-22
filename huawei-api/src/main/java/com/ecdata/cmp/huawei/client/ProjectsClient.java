package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.ProjectsListResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/projects")
public interface ProjectsClient {

    @PutMapping(path = "/get_project_list")
    @ApiOperation(value = "查vdc下的项目", notes = "查vdc下的项目")
    ProjectsListResponse getProjectList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                        @RequestBody RequestVO requestVO) throws IOException;
}

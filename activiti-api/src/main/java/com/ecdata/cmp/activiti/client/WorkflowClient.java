package com.ecdata.cmp.activiti.client;

import com.ecdata.cmp.activiti.ActivitiConstant;
import com.ecdata.cmp.activiti.dto.response.WorkflowListResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowStepResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2020-01-06
 */
@FeignClient(value = ActivitiConstant.SERVICE_NAME, path = "/v1/workflow")
public interface WorkflowClient {

    @GetMapping(path = "/list")
    @ApiOperation(value = "获取工作流列表", notes = "获取工作流列表")
    WorkflowListResponse list(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                              @RequestParam(value = "type", required = false) Integer type,
                              @RequestParam(value = "keyword", required = false) String keyword);

    @GetMapping(path = "/next_step")
    @ApiOperation(value = "获取下一步工作流步骤", notes = "获取下一步工作流步骤")
    WorkflowStepResponse getNextWorkflowStep(@RequestParam Long workflowId,
                                             @RequestParam Integer sort);

    @GetMapping(path = "/{id}/detail")
    @ApiOperation(value = "根据id查询工作流详情", notes = "根据id查询工作流详情")
    WorkflowResponse getDetailById(@PathVariable(value = "id") Long id);
}

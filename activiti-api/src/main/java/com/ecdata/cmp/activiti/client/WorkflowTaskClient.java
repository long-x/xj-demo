package com.ecdata.cmp.activiti.client;

import com.ecdata.cmp.activiti.ActivitiConstant;
import com.ecdata.cmp.activiti.dto.response.WorkflowTaskListResponse;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2020-01-06
 */
@FeignClient(value = ActivitiConstant.SERVICE_NAME, path = "/v1/workflow_task")
public interface WorkflowTaskClient {

    @PostMapping(path = "/add")
    @ApiOperation(value = "新增工作流任务", notes = "新增工作流任务")
    BaseResponse add(@RequestBody WorkflowTaskVO workflowTaskVO);

    @GetMapping(path = "/list")
    @ApiOperation(value = "获取工作流任务列表", notes = "获取工作流任务列表")
    WorkflowTaskListResponse list(@RequestParam(value = "workflowId", required = false) Long workflowId,
                                  @RequestParam(value = "workflowStep", required = false) Integer workflowStep,
                                  @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                  @RequestParam(value = "taskId", required = false) String taskId,
                                  @RequestParam(value = "isApproved", required = false) Integer isApproved,
                                  @RequestParam(value = "detailFlag", required = false, defaultValue = "0") Integer detailFlag);
}

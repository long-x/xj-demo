package com.ecdata.cmp.activiti.client;

import com.ecdata.cmp.activiti.ActivitiConstant;
import com.ecdata.cmp.activiti.dto.vo.webservice.WorkflowRequestInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxiaojian
 * @date 2020/4/13 10:59
 */
@FeignClient(value = ActivitiConstant.SERVICE_NAME, path = "/v1/oa")
public interface WebServiceClient {
    @PostMapping(path = "/doCreateWorkflowRequest")
    @ApiOperation(value = "创建工作流程", notes = "创建工作流程")
    String doCreateWorkflowRequest(@RequestBody WorkflowRequestInfoVO workflowRequestInfoVO);

    @GetMapping(path = "/getUserId")
    @ApiOperation(value = "获取人员ID", notes = "获取人员ID")
    String getUserId(@RequestParam(value = "filedType") String filedType,
                     @RequestParam(value = "filedValue") String filedValue);

    @PostMapping(path = "/submitWorkflowRequest")
    @ApiOperation(value = "提交工作流程", notes = "提交工作流程")
    String submitWorkflowRequest(@RequestBody WorkflowRequestInfoVO workflowRequestInfoVO);

}

package com.ecdata.cmp.activiti.client;

import com.ecdata.cmp.activiti.ActivitiConstant;
import com.ecdata.cmp.activiti.dto.ProcessDTO;
import com.ecdata.cmp.activiti.dto.request.CandidateRequest;
import com.ecdata.cmp.activiti.dto.request.CompleteTaskRequest;
import com.ecdata.cmp.activiti.dto.response.ActHistoricTaskInstanceListResponse;
import com.ecdata.cmp.activiti.dto.response.ActTaskListResponse;
import com.ecdata.cmp.activiti.dto.response.ActTaskPageResponse;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxinsheng
 * @since 2020-01-06
 */
@FeignClient(value = ActivitiConstant.SERVICE_NAME, path = "/v1/activiti")
public interface ActivitiClient {

    @PostMapping(path = "/start_process")
    @ApiOperation(value = "开始流程", notes = "开始流程")
    JSONObjectResponse startProcess(@RequestBody ProcessDTO processDTO);


    @PostMapping(path = "/complete_task")
    @ApiOperation(value = "完成任务", notes = "完成任务 (hasNextTask: true流程转到下一步，false流程完全结束)")
    JSONObjectResponse completeTask(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                    @RequestBody CompleteTaskRequest completeTask);

    @GetMapping(path = "/query_user_task")
    @ApiOperation(value = "查询用户任务", notes = "查询用户任务")
    ActTaskPageResponse queryUserTask(@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                      @RequestParam(value = "userId", required = false) Long userId,
                                      @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                      @RequestParam(value = "processOperation", required = false) String processOperation,
                                      @RequestParam(value = "processObject", required = false) String processObject,
                                      @RequestParam(value = "processVariablesFlag", defaultValue = "false") Boolean processVariablesFlag);

    @DeleteMapping(path = "/delete_process_instance")
    @ApiOperation(value = "删除流程实例", notes = "删除流程实例")
    BaseResponse deleteProcessInstance(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                       @RequestParam("processInstanceId") String processInstanceId,
                                       @RequestParam("deleteReason") String deleteReason,
                                       @RequestParam(value = "cascade", defaultValue = "0", required = false) Integer cascade);

    @GetMapping(path = "/query_history_task")
    @ApiOperation(value = "查询历史任务", notes = "查询历史任务")
    ActHistoricTaskInstanceListResponse queryHistoryTask(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                                         @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId,
                                                         @RequestParam(value = "taskDefinitionKey", required = false) String taskDefinitionKey,
                                                         @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                                         @RequestParam(value = "executionId", required = false) String executionId,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "assignee", required = false) String assignee,
                                                         @RequestParam(value = "tenantId", required = false) String tenantId,
                                                         @RequestParam(value = "userId", required = false) Long userId,
                                                         @RequestParam(value = "processOperation", required = false) String processOperation,
                                                         @RequestParam(value = "processObject", required = false) String processObject);

    @GetMapping(path = "/query_run_task")
    @ApiOperation(value = "查询运行中任务", notes = "查询运行中任务")
    ActTaskListResponse queryRunTask(@RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                     @RequestParam(value = "processVariablesFlag", defaultValue = "false") Boolean processVariablesFlag);

    @PostMapping(path = "/add_candidate")
    @ApiOperation(value = "添加候选", notes = "添加候选")
    ActTaskListResponse addCandidate(@RequestBody CandidateRequest candidateRequest);

    @PostMapping(path = "/del_candidate")
    @ApiOperation(value = "删除候选", notes = "删除候选")
    ActTaskListResponse delCandidateUser(@RequestBody CandidateRequest candidateRequest);

    @GetMapping("/query_process_task")
    @ApiOperation(value = "查询流程任务", notes = "查询流程任务")
    ActHistoricTaskInstanceListResponse queryProcessTask(@RequestParam(value = "processInstanceId") String processInstanceId);
}

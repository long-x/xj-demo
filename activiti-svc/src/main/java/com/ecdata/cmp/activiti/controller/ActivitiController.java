package com.ecdata.cmp.activiti.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.activiti.dto.DeployProcessDTO;
import com.ecdata.cmp.activiti.dto.ProcessDTO;
import com.ecdata.cmp.activiti.dto.request.AssigneeRequest;
import com.ecdata.cmp.activiti.dto.request.CandidateRequest;
import com.ecdata.cmp.activiti.dto.request.CompleteTaskRequest;
import com.ecdata.cmp.activiti.dto.request.MyApplicationRequest;
import com.ecdata.cmp.activiti.dto.response.*;
import com.ecdata.cmp.activiti.dto.vo.*;
import com.ecdata.cmp.activiti.entity.ActHistoricTaskInstanceEntity;
import com.ecdata.cmp.activiti.entity.Workflow;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;
import com.ecdata.cmp.activiti.service.*;
import com.ecdata.cmp.activiti.webService.ClientTest;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.api.MapListResponse;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.rpc.ServiceException;
import java.io.*;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author xuxinsheng
 * @since 2020-01-06
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/activiti")
@Api(tags = "??????????????????????????????")
public class ActivitiController {

    /**
     * ??????????????????Service
     */
    @Autowired
    private IActivitiService activitiService;
    /**
     * ???????????????
     */
    @Autowired
    private IWorkflowService workflowService;
    /**
     * ???????????????????????????
     */
    @Autowired
    private IWorkflowStepRightService workflowStepRightService;

    /**
     * ???????????????Service
     */
    @Autowired
    private IWorkflowTaskService workflowTaskService;

    /**
     * ?????????????????????Service
     */
    @Autowired
    private IWorkflowTaskCandidateService workflowTaskCandidateService;

    @PostMapping("/redeploy_workflow")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> redeployWorkflow() {
        String deploymentName = "?????????";
        String bpmnPath = "bpmn/workflow.bpmn";
        String pngPath = "bpmn/workflow.png";
        activitiService.deployProcess(deploymentName, bpmnPath, pngPath);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }


    @PostMapping("/test_demo")
    @ApiOperation(value = "????????????OA??????", notes = "????????????OA??????")
    public ResponseEntity<BaseResponse> testDemo() throws ServiceException, RemoteException {

        ClientTest test = new ClientTest();
        test.test();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }



    @PostMapping("/deploy_process")
    @ApiOperation(value = "????????????", notes = "????????????(?????????zip???????????????bpmn???png?????????????????????bpmn????????????)")
    public ResponseEntity<BaseResponse> deployProcess(@RequestBody DeployProcessDTO deployProcessDTO) {
        String deploymentName = deployProcessDTO.getDeploymentName();
        if (deployProcessDTO.getType() == 1) {
            String bpmnPath = "bpmn/" + deployProcessDTO.getBpmnName();
            String pngPath = "bpmn/" + deployProcessDTO.getPngName();
            activitiService.deployProcess(deploymentName, bpmnPath, pngPath);
        } else {
            String zipPath = "bpmn/" + deployProcessDTO.getZipName();
            activitiService.deployProcess(deploymentName, zipPath);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }

    @RequestMapping(value = "/deploy_upload_process", method = RequestMethod.POST)
    @ApiOperation(value = "???????????????zip??????", notes = "???????????????zip?????? ")
    public ResponseEntity<BaseResponse> deployUploadProcess(@RequestParam(value = "file") MultipartFile multipartFile,
                                                            @RequestParam String deploymentName) {
        BaseResponse baseResponse = new BaseResponse();
        ZipInputStream zipInputStream = null;
        try {

            String separator = System.getProperty("file.separator");
            String dirPath = new File("").getAbsolutePath() + separator + "activitiFile" + separator + deploymentName;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = multipartFile.getOriginalFilename();
            String filePath = dirPath + separator + fileName;
            File file = new File(filePath);
            multipartFile.transferTo(file);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            zipInputStream = new ZipInputStream(in);
            activitiService.deployProcess(deploymentName, zipInputStream);

            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } catch (IOException e) {
            log.error("upload error :{}", e);
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????zip????????????");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/latest_version_process_definition")
    @ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<ActProcessDefinitionListResponse> getLatestVersionProcessDefinition() {
        List<ActProcessDefinitionVO> list = activitiService.queryLatestVersionProcessDefinition();
        return ResponseEntity.status(HttpStatus.OK).body(new ActProcessDefinitionListResponse(list));
    }

    @PostMapping("/start_process")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<JSONObjectResponse> startProcess(@RequestBody ProcessDTO processDTO) {
        JSONObjectResponse response = new JSONObjectResponse();
        Long userId = processDTO.getUserId();
        String userDisplayName = processDTO.getUserDisplayName();
        if (userId == null) {
            userId = Sign.getUserId();
            userDisplayName = Sign.getUserDisplayName();
        }
        String processDefinitionKey = processDTO.getProcessDefinitionKey();
        if (StringUtils.isEmpty(processDefinitionKey)) {
            processDefinitionKey = IActivitiService.DEFAULT_PROCESS_DEFINITION_KEY;
        }
        String processOperation = processDTO.getProcessOperation();
        if (StringUtils.isEmpty(processOperation)) {
            processOperation = "apply";
        }
        String processObject = processDTO.getProcessObject();
        if (StringUtils.isEmpty(processObject)) {
            processObject = "process";
        }
        Long businessId = processDTO.getBusinessId();
        Map<String, Object> params = processDTO.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("userName", userDisplayName);
        params.put("businessDetail", processDTO.getBusinessDetail());
        params.put("businessGroupId", processDTO.getBusinessGroupId());
        params.put("businessGroupName", processDTO.getBusinessGroupName());
        params.put("processName", processDTO.getProcessName());
        params.put("notifyMessage", processDTO.getNotifyMessage());
        params.put("notifyDetail", processDTO.getNotifyDetail());
        Long processWorkflowId = processDTO.getProcessWorkflowId();
        if (processWorkflowId != null) {
            Workflow workflow = this.workflowService.getById(processWorkflowId);
            if (workflow == null) {
                response.setCode(ResultEnum.PARAM_VALID_ERROR.getCode());
                response.setMessage("?????????id?????????");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            if (workflow.getIsDisabled() == 1) {
                response.setCode(ResultEnum.PARAM_VALID_ERROR.getCode());
                response.setMessage("??????????????????");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        params.put("processWorkflowId", processWorkflowId);
        Integer processWorkflowStep = 1;
        params.put("processWorkflowStep", processWorkflowStep);
        String piId = activitiService.startProcess(processDefinitionKey, processOperation, processObject, businessId, userId, params);
        Integer applyFlag = processDTO.getApplyFlag();
        if (applyFlag != null && applyFlag == 1) {
            String taskId = activitiService.queryTaskId(processOperation, processObject, businessId);
            String outcome = processDTO.getOutcome(); // ??????bpmn???????????????outcome?????????
            if (StringUtils.isEmpty(outcome)) {
                outcome = "??????";
            }
            JSONObjectResponse r = activitiService.completeTask(taskId, outcome, userId, userDisplayName, processDTO.getComment(), null);
            if (!r.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
                response.setMessage("???????????????????????????????????????");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processInstanceId", piId);
        response.setData(jsonObject);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/query_user_task")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "userId", value = "???????????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processOperation", value = "?????????????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processObject", value = "?????????????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processVariablesFlag", value = "????????????????????????(true:????????????????????????;false:???????????????;)",
                    paramType = "query", dataType = "boolean", defaultValue = "false")
    })
    public ResponseEntity<ActTaskPageResponse> queryUserTask(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                             @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                             @RequestParam(required = false) Long userId,
                                                             @RequestParam(required = false) String processInstanceId,
                                                             @RequestParam(required = false) String processOperation,
                                                             @RequestParam(required = false) String processObject,
                                                             @RequestParam(defaultValue = "false") Boolean processVariablesFlag) {
        List<String> groups = this.workflowStepRightService.getUserRight(Sign.getUserId());
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", userId);
        variables.put("processInstanceId", processInstanceId);
        variables.put("processOperation", processOperation);
        variables.put("processObject", processObject);
        List<ActTaskVO> taskVOList = this.activitiService.queryUserTask(String.valueOf(Sign.getUserId()), groups, variables, false);
        PageVO<ActTaskVO> result = new PageVO<>(pageNo, pageSize, taskVOList);
        if (processVariablesFlag) {
            this.activitiService.getVariablesToList(result.getData());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ActTaskPageResponse(result));
    }

    @GetMapping("/query_line_outcome")
    @ApiOperation(value = "??????????????????", notes = "???????????????????????????????????????????????????")
    @ApiImplicitParam(name = "taskId", value = "??????id", paramType = "query", dataType = "string")
    public ResponseEntity<MapListResponse> queryLineOutcome(@RequestParam String taskId) {
        List<Map<String, Object>> result = this.activitiService.queryLineOutcomeByTaskId(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(new MapListResponse(result));
    }

    @PostMapping("/complete_task")
    @ApiOperation(value = "????????????", notes = "???????????? (hasNextTask: true????????????????????????false??????????????????)")
    public ResponseEntity<JSONObjectResponse> completeTask(@RequestBody CompleteTaskRequest completeTask) {
        String outcome = completeTask.getOutcome();
        if (StringUtils.isEmpty(outcome)) {
            JSONObjectResponse response = new JSONObjectResponse();
            response.setCode(ResultEnum.PARAM_MISS.getCode());
            response.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        Long userId = completeTask.getUserId();
        String userDisplayName = completeTask.getUserDisplayName();
        if (userId == null) {
            userId = Sign.getUserId();
            userDisplayName = Sign.getUserDisplayName();
        }
        String taskId = completeTask.getTaskId();
        Map<String, Object> params = completeTask.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        if (StringUtils.isNotEmpty(completeTask.getBusinessDetail())) {
            params.put("businessDetail", completeTask.getBusinessDetail());
        }
        if (StringUtils.isNotEmpty(completeTask.getNotifyMessage())) {
            params.put("notifyMessage", completeTask.getNotifyMessage());
        }
        if (StringUtils.isNotEmpty(completeTask.getNotifyDetail())) {
            params.put("notifyDetail", completeTask.getNotifyDetail());
        }
        JSONObjectResponse response = this.activitiService.completeTask(taskId, outcome, userId, userDisplayName, completeTask.getComment(), params);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/query_comments")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "??????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "businessKey", value = "??????key(??????apply:process:1)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processOperation", value = "?????????????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processObject", value = "?????????????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "businessId", value = "??????id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<ActCommentListResponse> queryComments(@RequestParam(required = false) String taskId,
                                                                @RequestParam(required = false) String processInstanceId,
                                                                @RequestParam(required = false) String businessKey,
                                                                @RequestParam(required = false) String processOperation,
                                                                @RequestParam(required = false) String processObject,
                                                                @RequestParam(required = false) Long businessId) {
        ActCommentListResponse response = new ActCommentListResponse();
        List<ActCommentVO> list;
        if (StringUtils.isNotEmpty(taskId)) {
            list = this.activitiService.queryCommentsByTaskId(taskId);
        } else if (StringUtils.isNotEmpty(processInstanceId)) {
            list = this.activitiService.queryCommentsByProcessInstanceId(processInstanceId);
        } else if (StringUtils.isNotEmpty(businessKey)) {
            list = this.activitiService.queryCommentsByBusinessKey(businessKey);
        } else {
            if (StringUtils.isEmpty(processOperation) || StringUtils.isEmpty(processObject) || businessId == null) {
                response.setCode(ResultEnum.PARAM_MISS.getCode());
                response.setMessage("??????????????????");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            list = this.activitiService.queryCommentsProcessTypeAndByBusinessId(processOperation, processObject, businessId);
        }
        response.setData(list);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/query_history_task")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "executionId", value = "??????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "assignee", value = "?????????????????????", paramType = "query", dataType = "string"),
//            @ApiImplicitParam(name = "category", value = "??????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tenantId", value = "??????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "???????????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "processOperation", value = "?????????????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processObject", value = "?????????????????????", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ActHistoricTaskInstanceListResponse> queryHistoryTask(@RequestParam(required = false) String processDefinitionId,
                                                                                @RequestParam(required = false) String taskDefinitionKey,
                                                                                @RequestParam(required = false) String processInstanceId,
                                                                                @RequestParam(required = false) String executionId,
                                                                                @RequestParam(required = false) String name,
                                                                                @RequestParam(required = false) String assignee,
//                                                                                @RequestParam(required = false) String category,
                                                                                @RequestParam(required = false) String tenantId,
                                                                                @RequestParam(required = false) Long userId,
                                                                                @RequestParam(required = false) String processOperation,
                                                                                @RequestParam(required = false) String processObject) {
        ActHistoricTaskInstanceEntity actHistoricTaskInstanceEntity = new ActHistoricTaskInstanceEntity();
        actHistoricTaskInstanceEntity.setProcessDefinitionId(processDefinitionId);
        actHistoricTaskInstanceEntity.setTaskDefinitionKey(taskDefinitionKey);
        actHistoricTaskInstanceEntity.setProcessInstanceId(processInstanceId);
        actHistoricTaskInstanceEntity.setExecutionId(executionId);
        actHistoricTaskInstanceEntity.setName(name);
        actHistoricTaskInstanceEntity.setAssignee(assignee);
//        actHistoricTaskInstanceEntity.setCategory(category);
        actHistoricTaskInstanceEntity.setTenantId(tenantId);
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("userId", userId);
        processVariables.put("processOperation", processOperation);
        processVariables.put("processObject", processObject);
        List<ActHistoricTaskInstanceVO> list =
                this.activitiService.queryHistoryTask(actHistoricTaskInstanceEntity, processVariables, null, null, true);
        return ResponseEntity.status(HttpStatus.OK).body(new ActHistoricTaskInstanceListResponse(list));
    }

    @DeleteMapping("/delete_process_instance")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deleteReason", value = "????????????", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "cascade", value = "??????(0:?????????;1:????????????;)", paramType = "query", dataType = "int", defaultValue = "0")
    })
    public ResponseEntity<BaseResponse> deleteProcessInstance(@RequestParam String processInstanceId,
                                                              @RequestParam String deleteReason,
                                                              @RequestParam(defaultValue = "0", required = false) Integer cascade) {
        this.activitiService.deleteProcessInstance(processInstanceId, deleteReason);
        if (cascade != null && cascade == 1) {
            this.activitiService.deleteHistoricProcessInstance(processInstanceId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }

    @DeleteMapping("/delete_historic_process_instance")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
    })
    public ResponseEntity<BaseResponse> deleteHistoricProcessInstance(@RequestParam String processInstanceId) {
        this.activitiService.deleteHistoricProcessInstance(processInstanceId);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }

    @PostMapping("/add_candidate")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> addCandidate(@RequestBody CandidateRequest candidateRequest) {
        BaseResponse baseResponse = new BaseResponse();
        String taskId = candidateRequest.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Long candidateId = candidateRequest.getCandidateId();
        if (candidateId == null) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Integer type = candidateRequest.getType();
        if (type == null) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        if (type == 1) {
            this.activitiService.addCandidateUser(taskId, String.valueOf(candidateId));
        } else {
            String groupId = type + IActivitiService.SEPARATOR + candidateId;
            this.activitiService.addCandidateGroup(taskId, groupId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PostMapping("/del_candidate")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> delCandidateUser(@RequestBody CandidateRequest candidateRequest) {
        BaseResponse baseResponse = new BaseResponse();
        String taskId = candidateRequest.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Long candidateId = candidateRequest.getCandidateId();
        if (candidateId == null) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Integer type = candidateRequest.getType();
        if (type == null) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("??????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        if (type == 1) {
            this.activitiService.deleteCandidateUser(taskId, String.valueOf(candidateId));
        } else {
            String groupId = type + IActivitiService.SEPARATOR + candidateId;
            this.activitiService.deleteCandidateGroup(taskId, groupId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }


    @PostMapping("/handle_task_assignee")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    public ResponseEntity<BaseResponse> handleTaskAssignee(@RequestBody AssigneeRequest assigneeRequest) {
        BaseResponse baseResponse = new BaseResponse();
        String taskId = assigneeRequest.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        activitiService.handleTaskAssignee(taskId, assigneeRequest.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/query_my_request")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "status", value = "??????(1:??????;2:??????;)", paramType = "query", dataType = "int")
    public ResponseEntity<MyApplicationPageResponse> queryMyRequest(@ApiParam @ModelAttribute MyApplicationRequest myApplicationRequest,
                                                                    @RequestParam(required = false) Integer status) throws ParseException {
        PageVO<MyApplicationVO> data = this.activitiService.queryMyRequest(Sign.getUserId(), myApplicationRequest, status);
        return ResponseEntity.status(HttpStatus.OK).body(new MyApplicationPageResponse(data));
    }

    @GetMapping("/query_my_backlog")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<ActTaskPageResponse> queryMyBacklog(@ApiParam @ModelAttribute MyApplicationRequest myApplicationRequest)
            throws ParseException {
        List<String> groups = this.workflowStepRightService.getUserRight(Sign.getUserId());
        Map<String, Object> params = this.activitiService.transformToParams(myApplicationRequest);
        params.put("notProcessWorkflowStep", 1);
        List<ActTaskVO> taskVOList = this.activitiService.queryUserTask(String.valueOf(Sign.getUserId()), groups, params, true);
        Integer pageNo = myApplicationRequest.getPageNo() == null ? 1 : myApplicationRequest.getPageNo();
        Integer pageSize = myApplicationRequest.getPageNo() == null ? Constants.TWENTY : myApplicationRequest.getPageSize();
        PageVO<ActTaskVO> result = new PageVO<>(pageNo, pageSize, taskVOList);
        this.activitiService.getVariablesToList(result.getData());
        return ResponseEntity.status(HttpStatus.OK).body(new ActTaskPageResponse(result));
    }

    @GetMapping("/query_my_approved")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public ResponseEntity<MyApplicationPageResponse> queryMyApproved(@ApiParam @ModelAttribute MyApplicationRequest myApplicationRequest)
            throws ParseException {
        PageVO<MyApplicationVO> data = this.activitiService.queryMyApproved(Sign.getUserId(), myApplicationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MyApplicationPageResponse(data));
    }

    @GetMapping("/query_run_task")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "processVariablesFlag", value = "????????????????????????(true:????????????????????????;false:???????????????;)",
                    paramType = "query", dataType = "boolean", defaultValue = "false")
    })
    public ResponseEntity<ActTaskListResponse> queryRunTask(@RequestParam String processInstanceId,
                                                            @RequestParam(defaultValue = "false") Boolean processVariablesFlag) {
        List<ActTaskVO> list = this.activitiService.queryRunTask(processInstanceId, processVariablesFlag);
        return ResponseEntity.status(HttpStatus.OK).body(new ActTaskListResponse(list));
    }

    @GetMapping("/query_process_task")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ActHistoricTaskInstanceListResponse> queryProcessTask(@RequestParam String processInstanceId) {
        ActHistoricTaskInstanceEntity actHistoricTaskInstanceEntity = new ActHistoricTaskInstanceEntity();
        actHistoricTaskInstanceEntity.setProcessInstanceId(processInstanceId);
        List<ActHistoricTaskInstanceVO> retList =
                this.activitiService.queryHistoryTask(actHistoricTaskInstanceEntity, null, null, null, true);
        if (retList != null && retList.size() > 0) {
            QueryWrapper<WorkflowTask> workflowTaskQueryWrapper = new QueryWrapper<>();
            workflowTaskQueryWrapper.lambda().eq(WorkflowTask::getProcessInstanceId, processInstanceId).eq(WorkflowTask::getType, Constants.TWO);
            List<WorkflowTask> workflowTaskList = this.workflowTaskService.list(workflowTaskQueryWrapper);
            if (workflowTaskList != null && workflowTaskList.size() > 0) {
                int size = retList.size();
                for (int i = 0; i < size; i++) {
                    ActHistoricTaskInstanceVO actHistoricTaskInstanceVO = retList.get(i);
                    String assignee = actHistoricTaskInstanceVO.getAssignee();
                    String taskId = actHistoricTaskInstanceVO.getId();
                    for (WorkflowTask workflowTask : workflowTaskList) {
                        if (taskId.equals(workflowTask.getTaskId())) {
                            QueryWrapper<WorkflowTaskCandidate> candidateQueryWrapper = new QueryWrapper<>();
                            candidateQueryWrapper.lambda().eq(WorkflowTaskCandidate::getWorkflowTaskId, workflowTask.getId())
                                    .eq(WorkflowTaskCandidate::getIsApproved, 1)
                                    .orderByDesc(WorkflowTaskCandidate::getUpdateTime);
                            List<WorkflowTaskCandidate> candidateList = this.workflowTaskCandidateService.list(candidateQueryWrapper);
                            if (candidateList != null && candidateList.size() > 0) {
                                for (WorkflowTaskCandidate candidate : candidateList) {
                                    Long relateId = candidate.getRelateId();
                                    if (relateId != null && !StringUtils.equals(assignee, relateId.toString())) {
                                        ActHistoricTaskInstanceVO vo = new ActHistoricTaskInstanceVO();
                                        BeanUtils.copyProperties(actHistoricTaskInstanceVO, vo);
                                        vo.setAssignee(relateId.toString()).setOutcome(candidate.getOutcome()).setComment(candidate.getComment());
                                        Date updateTime = candidate.getUpdateTime();
                                        if (updateTime != null) {
                                            vo.setClaimTime(updateTime).setEndTime(updateTime)
                                                    .setDurationInMillis(updateTime.getTime() - vo.getStartTime().getTime());
                                        }
                                        i++;
                                        if (i < size) {
                                            retList.add(i, vo);
                                        } else {
                                            retList.add(vo);
                                        }
                                        size++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ActHistoricTaskInstanceListResponse(retList));
    }

}

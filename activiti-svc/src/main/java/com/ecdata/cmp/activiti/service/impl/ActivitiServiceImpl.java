package com.ecdata.cmp.activiti.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.activiti.dto.request.MyApplicationRequest;
import com.ecdata.cmp.activiti.dto.vo.ActCommentVO;
import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
import com.ecdata.cmp.activiti.dto.vo.ActProcessDefinitionVO;
import com.ecdata.cmp.activiti.dto.vo.ActTaskVO;
import com.ecdata.cmp.activiti.dto.vo.MyApplicationVO;
import com.ecdata.cmp.activiti.entity.ActDeploymentEntity;
import com.ecdata.cmp.activiti.entity.ActHistoricTaskInstanceEntity;
import com.ecdata.cmp.activiti.entity.ActProcessDefinitionEntity;
import com.ecdata.cmp.activiti.service.IActivitiService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskCandidateService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskService;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfoQuery;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

/**
 * @author xuxinsheng
 * @since 2019-06-27
 */
@Service
@Slf4j
public class ActivitiServiceImpl implements IActivitiService {

    /**
     * 租户默认id
     */
    @Value("${tenant.defaultId}")
    private String defaultId;

    /**
     * 存储Service
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 运行时Service
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 任务Service
     */
    @Autowired
    private TaskService taskService;

    /**
     * 历史Service
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 身份Service
     */
    @Autowired
    private IdentityService identityService;

    /**
     * form Service
     */
    @Autowired
    private FormService formService;

    /**
     * 管理Service
     */
    @Autowired
    private ManagementService managementService;

    /**
     * workflowTaskService
     */
    @Autowired
    private IWorkflowTaskService workflowTaskService;

    /**
     * workflowTaskCandidateService
     */
    @Autowired
    private IWorkflowTaskCandidateService workflowTaskCandidateService;

    /**
     * 获取租户id
     *
     * @return 租户id
     */
    private String getTenantId() {
        String tenantId;
        try {
            tenantId = Sign.getTenantId().toString();
        } catch (Exception e) {
            tenantId = this.defaultId;
        }
        return StringUtils.isEmpty(tenantId) ? "10000" : tenantId;
    }

    /**
     * 根据任务id获取流程实例
     *
     * @param taskId 任务id
     * @return 流程实例
     */
    private ProcessInstance getProcessInstanceByTaskId(String taskId) {
        // 1.根据任务id查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.从任务里面取出流程实例id
        String processInstanceId = task.getProcessInstanceId();
        // 3.根据流程实例id查询流程实例
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    private String queryBusinessKeyByTaskId(String taskId) {
        ProcessInstance processInstance = this.getProcessInstanceByTaskId(taskId);
        // 根据设定的是processType:1
        return processInstance.getBusinessKey();
    }

    /**
     * 获取业务信息
     *
     * @param businessKey 如：apply:process:1
     * @return 业务信息
     */
    private String[] queryBusinessInfo(String businessKey) {
        return businessKey.split(IActivitiService.SEPARATOR);
    }

    /**
     * 获取业务键
     *
     * @param processOperation 流程操作
     * @param processObject    流程对象
     * @param businessId       业务id
     * @return 业务键
     */
    private String queryBusinessKey(String processOperation, String processObject, Long businessId) {
        return processOperation + IActivitiService.SEPARATOR + processObject + IActivitiService.SEPARATOR + businessId;
    }

    public void deployProcess(String deploymentName, String zipPath) {
        ZipInputStream zipInputStream = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(zipPath);
            zipInputStream = new ZipInputStream(inputStream);
            this.deployProcess(deploymentName, zipInputStream);
        } catch (Exception e) {
            log.error("部署失败，部署名：{}", deploymentName);
            throw e;
        } finally {
            try {
                if (zipInputStream != null) {
                    zipInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void deployProcess(String deploymentName, ZipInputStream zipInputStream) {
        log.info("开始部署, 名称:{}", deploymentName);
        String tenantId = this.getTenantId();
        Deployment deployment = repositoryService.createDeployment().name(deploymentName)
                .addZipInputStream(zipInputStream)
                .tenantId(tenantId)
                .deploy();
        log.info("部署成功,流程部署id:{}, name:{}, 租户id:{}", deployment.getId(), deploymentName, tenantId);
    }

    @Override
    public void deployProcess(String deploymentName, String bpmnPath, String pngPath) {
        // path: "/workflow.bpmn" 无/从当前包找，有/从classpath根目录找
        log.info("开始部署, 名称:{}", deploymentName);
        String tenantId = this.getTenantId();
        Deployment deployment = repositoryService.createDeployment().name(deploymentName)
                .addClasspathResource(bpmnPath)
                .addClasspathResource(pngPath)
                .tenantId(tenantId)
                .deploy();
        log.info("部署成功,流程部署id:{}, name:{}, 租户id:{}", deployment.getId(), deploymentName, tenantId);
    }

    @Override
    public List<ActProcessDefinitionEntity> queryProcessDefinition(ActProcessDefinitionEntity actProcessDefinitionEntity) {
        // 查询流程定义 act_re_procdef
        List<ActProcessDefinitionEntity> list = new ArrayList<>();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getId())) {
            processDefinitionQuery.processDefinitionId(actProcessDefinitionEntity.getId());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getCategory())) {
            processDefinitionQuery.processDefinitionCategory(actProcessDefinitionEntity.getCategory());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getName())) {
            processDefinitionQuery.processDefinitionName(actProcessDefinitionEntity.getName());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getKey())) {
            processDefinitionQuery.processDefinitionKey(actProcessDefinitionEntity.getKey());
        }
        if (null != actProcessDefinitionEntity.getVersion()) {
            processDefinitionQuery.processDefinitionVersion(actProcessDefinitionEntity.getVersion());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getDeploymentId())) {
            processDefinitionQuery.deploymentId(actProcessDefinitionEntity.getDeploymentId());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getResourceName())) {
            processDefinitionQuery.processDefinitionResourceName(actProcessDefinitionEntity.getResourceName());
        }
        if (StringUtils.isNotEmpty(actProcessDefinitionEntity.getTenantId())) {
            processDefinitionQuery.processDefinitionTenantId(actProcessDefinitionEntity.getTenantId());
        }
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();

        if (null != processDefinitionList && processDefinitionList.size() > 0) {
            for (ProcessDefinition processDefinition : processDefinitionList) {
                ActProcessDefinitionEntity pd = new ActProcessDefinitionEntity();
                BeanUtils.copyProperties(processDefinition, pd);
                list.add(pd);
            }
        }

        return list;
    }

    @Override
    public List<ActProcessDefinitionVO> queryLatestVersionProcessDefinition() {
        List<ActProcessDefinitionVO> list = new ArrayList<>();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        if (null != processDefinitionList && processDefinitionList.size() > 0) {
            for (ProcessDefinition processDefinition : processDefinitionList) {
                ActProcessDefinitionVO pd = new ActProcessDefinitionVO();
                BeanUtils.copyProperties(processDefinition, pd);
                list.add(pd);
            }
        }
        return list;
    }

    @Override
    public List<ActProcessDefinitionEntity> queryProcessDefinitionByDeploymentName(String deploymentName) {
        // 查询流程定义
        List<ActProcessDefinitionEntity> list = new ArrayList<>();

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
                .deploymentName(deploymentName)
                .deploymentTenantId(this.getTenantId())
                .list();
        Set<String> deploymentIds = new HashSet<>();
        for (Deployment deployment : deploymentList) {
            deploymentIds.add(deployment.getId());
        }
        if (deploymentIds.size() > 0) {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                    .deploymentIds(deploymentIds)
                    .list();
            if (null != processDefinitionList && processDefinitionList.size() > 0) {
                for (ProcessDefinition processDefinition : processDefinitionList) {
                    ActProcessDefinitionEntity actProcessDefinitionEntity = new ActProcessDefinitionEntity();
                    BeanUtils.copyProperties(processDefinition, actProcessDefinitionEntity);
                    list.add(actProcessDefinitionEntity);
                }
            }
        }
        return list;
    }

    @Override
    public ActProcessDefinitionEntity queryProcessDefinitionByTaskId(String taskId) {
        // 根据任务id查询流程定义对象
        ProcessInstance processInstance = this.getProcessInstanceByTaskId(taskId);
        String processDefinitionId = processInstance.getProcessDefinitionId();
        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        ActProcessDefinitionEntity actProcessDefinitionEntity = new ActProcessDefinitionEntity();
        BeanUtils.copyProperties(processDefinition, actProcessDefinitionEntity);
        return actProcessDefinitionEntity;
    }

    @Override
    public List<ActDeploymentEntity> queryProcessDeployment(ActDeploymentEntity actDeploymentEntity) {
        // 查询流程部署 act_re_deployment
        List<ActDeploymentEntity> list = new ArrayList<>();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        if (StringUtils.isNotEmpty(actDeploymentEntity.getId())) {
            deploymentQuery.deploymentId(actDeploymentEntity.getId());
        }
        if (StringUtils.isNotEmpty(actDeploymentEntity.getName())) {
            // deploymentNameLike("%" + deploymentName + "%")
            deploymentQuery.deploymentName(actDeploymentEntity.getName());
        }
        if (StringUtils.isNotEmpty(actDeploymentEntity.getCategory())) {
            deploymentQuery.deploymentCategory(actDeploymentEntity.getCategory());
        }
        if (StringUtils.isNotEmpty(actDeploymentEntity.getTenantId())) {
            deploymentQuery.deploymentTenantId(actDeploymentEntity.getTenantId());
        }
        List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().desc().list();
        if (null != deploymentList && deploymentList.size() > 0) {
            for (Deployment deployment : deploymentList) {
                ActDeploymentEntity d = new ActDeploymentEntity();
                BeanUtils.copyProperties(deployment, d);
                list.add(d);
            }
        }
        return list;
    }

    @Override
    public InputStream queryProcessDeploymentImage(String deploymentId) {
        // 获取流程部署图片
        // 1.根据部署id查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        // 2.从流程定义对象获取图片名字
        String diagramResourceName = processDefinition.getDiagramResourceName();
        // 3.使用部署id和图片名称去查询图片流
        return repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
//        repositoryService.getProcessDiagram(processDefinitionId);
    }

    @Override
    public void deleteProcessDeploy(String deploymentId, boolean cascade) {
        // true:级联删除正在执行的流程 act_ru_*和act_hi_*   false:如果有执行过流程，会报错
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    @Override
    public void deleteProcessDeployByProcessDefinitionKey(String processDefinitionKey, boolean cascade) {
        // 删除流程部署
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).list();
        if (null != list && list.size() > 0) {
            for (ProcessDefinition processDefinition : list) {
                this.deleteProcessDeploy(processDefinition.getDeploymentId(), cascade);
            }
        }
    }

    @Override
    public String startProcess(String processDefinitionKey,
                               String processOperation,
                               String processObject,
                               Long businessId,
                               Long userId,
                               Map<String, Object> params) {
        // 开始流程 act_re_procdef
//        runtimeService.startProcessInstanceById(processInstanceById);
//        String processDefinitionKey = "workflow";

        String businessKey = this.queryBusinessKey(processOperation, processObject, businessId);
        Map<String, Object> variables = new HashMap<>();
        if (params != null && params.size() > 0) {
            variables.putAll(params);
        }
        // 设置流程变量去设置下个任务的办理人
        // 如果是组任务，这变量用逗号隔开 如  ”1,2,3“
        variables.put("userId", userId);
        variables.put("processOperation", processOperation);
        variables.put("processObject", processObject);
        variables.put("businessId", businessId);
        variables.put("submittedFlag", false); // 是否提交申请过
        //act_ru_execution
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, variables, this.getTenantId());
        return processInstance.getId();
        // 更新业务状态
    }

    @Override
    public String queryTaskId(String processOperation, String processObject, Long businessId) {
        // 根据业务id查询任务id
        String businessKey = this.queryBusinessKey(processOperation, processObject, businessId);
        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        if (taskList != null && taskList.size() > 0) {
            return taskList.get(0).getId();
        }
        return null;
    }

    @Override
    public List<Task> queryAssigneeTask(String assignee, Map<String, Object> variables) {
        // 查询任务  assignee--userId  act_ru_task
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskAssignee(assignee).taskTenantId(this.getTenantId());
        this.setTaskInfoQueryCondition(taskQuery, variables);
        return taskQuery.orderByTaskCreateTime().desc().list();
    }

    @Override
    public List<Task> queryCandidateUserTask(String candidateUser, Map<String, Object> variables) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateUser(candidateUser).taskTenantId(this.getTenantId());
        this.setTaskInfoQueryCondition(taskQuery, variables);
        return taskQuery.orderByTaskCreateTime().desc().list();
    }

    @Override
    public List<Task> queryCandidateGroupTask(List<String> candidateGroups, Map<String, Object> variables) {
        if (candidateGroups == null || candidateGroups.size() == 0) {
            return new ArrayList<>();
        }
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateGroupIn(candidateGroups).taskTenantId(this.getTenantId());
        this.setTaskInfoQueryCondition(taskQuery, variables);
        return taskQuery.orderByTaskCreateTime().desc().list();
    }

    private void toNotRepeat(List<ActTaskVO> taskList, Set<String> taskIds, List<Task> original) {
        if (null != original && original.size() > 0) {
            for (Task task : original) {
                String taskId = task.getId();
                if (taskIds.contains(taskId)) {
                    continue;
                }
                taskIds.add(taskId);
                ActTaskVO actTaskVO = new ActTaskVO();
                BeanUtils.copyProperties(task, actTaskVO, "variables");
                taskList.add(actTaskVO);
            }
        }
    }

    private void setTaskInfoQueryCondition(TaskInfoQuery taskInfoQuery, Map<String, Object> variables) {
        if (variables != null && variables.size() > 0) {
            Date startTime = (Date) variables.get("startTime");
            if (startTime != null) {
                taskInfoQuery.taskCreatedAfter(startTime);
            }
            Date endTime = (Date) variables.get("endTime");
            if (endTime != null) {
                taskInfoQuery.taskCreatedBefore(endTime);
            }
            String processInstanceId = (String) variables.get("processInstanceId");
            if (StringUtils.isNotEmpty(processInstanceId)) {
                taskInfoQuery.processInstanceId(processInstanceId);
            }

            Long userId = (Long) variables.get("userId");
            if (userId != null) {
                taskInfoQuery.processVariableValueEquals("userId", userId);
            }
            String processOperation = (String) variables.get("processOperation");
            if (StringUtils.isNotEmpty(processOperation)) {
                taskInfoQuery.processVariableValueEquals("processOperation", processOperation);
            }
            String processObject = (String) variables.get("processObject");
            if (StringUtils.isNotEmpty(processObject)) {
                taskInfoQuery.processVariableValueEquals("processObject", processObject);
            }
            Long businessGroupId = (Long) variables.get("businessGroupId");
            if (businessGroupId != null) {
                taskInfoQuery.processVariableValueEquals("businessGroupId", businessGroupId);
            }
            String businessGroupName = (String) variables.get("businessGroupName");
            if (StringUtils.isNotEmpty(businessGroupName)) {
                taskInfoQuery.processVariableValueEquals("businessGroupName", businessGroupName);
            }
            String processName = (String) variables.get("processName");
            if (StringUtils.isNotEmpty(processName)) {
                taskInfoQuery.processVariableValueEquals("processName", processName);
            }
            Long workorderId = (Long) variables.get("workorderId");
            if (workorderId != null) {
                taskInfoQuery.processVariableValueEquals("workorderId", workorderId);
            }
            String businessDetail = (String) variables.get("businessDetail");
            if (StringUtils.isNotEmpty(businessDetail)) {
                taskInfoQuery.processVariableValueLike("businessDetail", "%" + businessDetail + "%");
            }
            Integer notProcessWorkflowStep = (Integer) variables.get("notProcessWorkflowStep");
            if (notProcessWorkflowStep != null) {
                taskInfoQuery.processVariableValueNotEquals("processWorkflowStep", notProcessWorkflowStep);
            }
        }
    }

    @Override
    public List<ActTaskVO> queryUserTask(String userId, List<String> groups, Map<String, Object> variables, boolean backlogFlag) {
        List<ActTaskVO> taskList = new ArrayList<>();
        Set<String> taskIds = new HashSet<>();
        if (backlogFlag) {
            List<String> list = new ArrayList<>();
            list.add("1:" + userId);
            if (groups != null && groups.size() > 0) {
                list.addAll(groups);
            }
            List<Map<String, Object>> backlogList = this.workflowTaskService.queryBacklog(list);
            if (backlogList != null && backlogList.size() > 0) {
                List<String> piIds = new ArrayList<>();
                for (Map<String, Object> backlog : backlogList) {
                    piIds.add(String.valueOf(backlog.get("processInstanceId")));
                    Object isApproved = backlog.get("isApproved");
                    if (isApproved != null && ("true".equalsIgnoreCase(isApproved.toString()) || "1".equalsIgnoreCase(isApproved.toString()))
                            && backlog.get("taskId") != null) {
                        taskIds.add(backlog.get("taskId").toString());
                    }
                }
                TaskQuery taskQuery = taskService.createTaskQuery();
                taskQuery.taskTenantId(this.getTenantId()).processInstanceIdIn(piIds);
                this.setTaskInfoQueryCondition(taskQuery, variables);
                List<Task> backlogTaskList = taskQuery.orderByTaskCreateTime().desc().list();
                this.toNotRepeat(taskList, taskIds, backlogTaskList);
                if (taskList.size() > 0) {
                    for (ActTaskVO taskVO : taskList) {
                        Long candidateId = null;
                        for (Map<String, Object> backlog : backlogList) {
                            if (taskVO.getId().equals(String.valueOf(backlog.get("taskId")))) {
                                taskVO.setWorkflowTaskId(Long.valueOf(backlog.get("workflowTaskId").toString()));
                                candidateId = Long.valueOf(backlog.get("candidateId").toString());
                                taskVO.setWorkflowTaskCandidateId(candidateId);
                            }
                        }
                        taskVO.setSupportList(workflowTaskCandidateService.getChildren(candidateId));
                    }
                }
            }
        }

        List<Task> assigneeTask = this.queryAssigneeTask(userId, variables);
        List<Task> candidateUserTask = this.queryCandidateUserTask(userId, variables);
        List<Task> candidateGroupTask = this.queryCandidateGroupTask(groups, variables);
        this.toNotRepeat(taskList, taskIds, assigneeTask);
        this.toNotRepeat(taskList, taskIds, candidateUserTask);
        this.toNotRepeat(taskList, taskIds, candidateGroupTask);

        taskList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));

        return taskList;
    }

    @Override
    public Map<String, Object> transformToParams(MyApplicationRequest request) throws ParseException {
        Map<String, Object> params = new HashMap<>();
        String startTime = request.getStartTime();
        if (StringUtils.isNotEmpty(startTime)) {
            params.put("startTime", DateUtil.parseStr(startTime));
        }
        String endTime = request.getEndTime();
        if (StringUtils.isNotEmpty(endTime)) {
            params.put("endTime", DateUtil.parseStr(endTime));
        }
        params.put("businessGroupId", request.getBusinessGroupId());
        params.put("businessGroupName", request.getBusinessGroupName());
        params.put("processInstanceId", request.getProcessInstanceId());
        params.put("workorderId", request.getWorkorderId());
        params.put("processName", request.getProcessName());
        params.put("processOperation", request.getProcessOperation());
        params.put("processObject", request.getProcessObject());
        params.put("businessDetail", request.getBusinessDetail());
        return params;
    }

    @Override
    public PageVO<MyApplicationVO> queryMyRequest(Long userId, MyApplicationRequest request, Integer status) throws ParseException {
        List<MyApplicationVO> allList = new ArrayList<>();

        Map<String, Object> params = this.transformToParams(request);
        params.put("userId", userId);

        if (status == null || status == 1) {
            TaskQuery taskQuery = taskService.createTaskQuery();
            taskQuery.taskTenantId(this.getTenantId());
            this.setTaskInfoQueryCondition(taskQuery, params);
            List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().list();
            if (taskList != null && taskList.size() > 0) {
                for (Task task : taskList) {
                    MyApplicationVO myApplicationVO = new MyApplicationVO();
                    BeanUtils.copyProperties(task, myApplicationVO);
                    myApplicationVO.setStartTime(task.getCreateTime());
                    myApplicationVO.setHistoryFlag(false);
                    allList.add(myApplicationVO);
                }
            }
        }

        if (status == null || status == Constants.TWO) {
            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
            this.setTaskInfoQueryCondition(historicTaskInstanceQuery, params);
            historicTaskInstanceQuery.taskName("申请");
            List<HistoricTaskInstance> htiList = historicTaskInstanceQuery.processFinished().orderByTaskCreateTime().desc().list();
            if (null != htiList && htiList.size() > 0) {
                for (HistoricTaskInstance historicTaskInstance : htiList) {
                    MyApplicationVO myApplicationVO = new MyApplicationVO();
                    BeanUtils.copyProperties(historicTaskInstance, myApplicationVO);
                    myApplicationVO.setHistoryFlag(true);
                    allList.add(myApplicationVO);
                }
            }
        }

        allList.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
        Integer pageNo = request.getPageNo() == null ? 1 : request.getPageNo();
        final int tem = 10000;
        Integer pageSize = request.getPageSize() == null ? tem : request.getPageSize();
        PageVO<MyApplicationVO> result = new PageVO<>(pageNo, pageSize, allList);
        List<MyApplicationVO> data = result.getData();
        if (data != null && data.size() > 0) {
            for (MyApplicationVO vo : data) {
                String processInstanceId = vo.getProcessInstanceId();
                Map<String, Object> processVariables = this.getProcessVariables(processInstanceId);
                vo.setProcessVariables(processVariables);
                vo.setSubmittedFlag((Boolean) processVariables.get("submittedFlag"));

                if (vo.getHistoryFlag()) {
                    HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
                    vo.setEndTime(hpi.getEndTime());
                    vo.setDurationInMillis(hpi.getDurationInMillis());
                    vo.setDeleteReason(hpi.getDeleteReason());
                }
            }
        }

        return result;
    }

    @Override
    public List<ActTaskVO> queryRunTask(String processInstanceId, boolean processVariablesFlag) {
        List<ActTaskVO> taskVOList = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskTenantId(this.getTenantId()).processInstanceId(processInstanceId);
        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().list();
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                ActTaskVO taskVO = new ActTaskVO();
                BeanUtils.copyProperties(task, taskVO, "variables");
                if (processVariablesFlag) {
                    taskVO.setVariables(this.getVariablesByTaskId(taskVO.getId()));
                }
                taskVOList.add(taskVO);
            }
        }
        return taskVOList;
    }

    @Override
    public void taskClaim(String taskId, Long userId) {
        // userId 由Candidate候选者 变成 assignee分配到任务的人
        taskService.claim(taskId, String.valueOf(userId));
    }

    @Override
    public void taskClaimBack(String taskId) {
        // 任务拾取回退 (只有主任务才能回退)
//        taskService.unclaim(taskId);
        taskService.setAssignee(taskId, null);
    }

    @Override
    public void handleTaskAssignee(String taskId, Long userId) {
        if (userId == null) {
            taskService.setAssignee(taskId, null);
        } else {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (StringUtils.isNotEmpty(task.getAssignee())) {
                taskService.setAssignee(taskId, null);
            }
            taskService.setAssignee(taskId, userId.toString());
        }
    }

    @Override
    public List<IdentityLink> queryGroupUser(String taskId) {
        // 查询任务的组用户
        // act_ru_identitylink 的type 为 candidate 而不是 participant
        // 如果任务已拾取，则也会查询type为assignee的分配到任务的人, 即代理人会出现2次，只是type不一样
        return taskService.getIdentityLinksForTask(taskId);
    }

    @Override
    public void addCandidateUser(String taskId, String userId) {
        // 添加候选用户
        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        // 添加候选组
        taskService.addCandidateGroup(taskId, groupId);
    }

    @Override
    public void deleteCandidateUser(String taskId, String userId) {
        // 删除候选用户 act_ru_identitylink type为participant的userId被删除，但是type为candidate还存留
        taskService.deleteCandidateUser(taskId, userId);
    }

    @Override
    public void deleteCandidateGroup(String taskId, String groupId) {
        taskService.deleteCandidateGroup(taskId, groupId);
    }

    public void completeTask(String taskId) {
        taskService.complete(taskId);

    }

    @Override
    public JSONObjectResponse completeTask(String taskId, String outcome, Long userId, String userName, String comment, Map<String, Object> params) {
        JSONObjectResponse response = new JSONObjectResponse();
        Boolean hasNextTask = null; // true: 完成任务后流程还未结束  false:完成任务后流程结束
//        String outcome = "同意"; // 连接名称
//        String comment = "批注";
        // 1.根据任务id查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 2.从任务里面取出流程实例id
        String processInstanceId = task.getProcessInstanceId();
        Integer processWorkflowStep = null;
        Map<String, Object> processVariables = this.getVariablesByTaskId(taskId);
        if (processVariables != null && processVariables.size() > 0) {
            processWorkflowStep = (Integer) processVariables.get("processWorkflowStep");
        }

        try {
            // 兼容没有token时候的调用
            Sign.setCurrentTenantId(Long.valueOf(this.getTenantId()));
            boolean completeFlag = this.workflowTaskService.approveTask(
                    processInstanceId, processWorkflowStep, taskId, outcome, userId, userName, comment);
            if (completeFlag) {
                if (StringUtils.isEmpty(task.getAssignee())) {
                    // 还未指定受理人, 需要先指定
                    this.taskClaim(taskId, userId);
                    //委托, 进行这个操作后，受理人(userId)变成委托人(owner), 其他人变成受理人(assignee)
                    // taskService.delegateTask(task.getId(), "其他人");
                }
                // 3.设置批注人
                Authentication.setAuthenticatedUserId(String.valueOf(userId));
                // 4.添加批注信息
                if (StringUtils.isNotEmpty(comment)) {
                    taskService.addComment(taskId, processInstanceId, comment); // comment为空会报错
                }

                // 5.完成任务,并指定流程变量
                Map<String, Object> variables = new HashMap<>();
                if (params != null && params.size() > 0) {
                    variables.putAll(params);
                }
                variables.put("outcome", outcome);
                variables.put("submittedFlag", true); // 表示流程提交过
                ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId);
                // 完成任务前先获取到业务id
//        String[] info = this.queryBusinessInfo(processInstanceQuery.singleResult().getBusinessKey());
//        Integer businessId = Integer.valueOf(info[2]);

                taskService.complete(taskId, variables);

                // 6.判断任务是否结束 并更新流程状态

                hasNextTask =  null != processInstanceQuery.singleResult();
            } else {
                hasNextTask =  true;
            }
        } catch (Exception e) {
            log.error("工作流任务审批任务异常", e);
            response.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            response.setMessage("审批异常");
        } finally {
            Sign.removeCurrentTenantId();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hasNextTask", hasNextTask);
        response.setData(jsonObject);
        return response;
    }


    @Override
    public ActCommentVO queryCommentByTaskId(String taskId) {
        ActCommentVO actCommentVO = new ActCommentVO();
        List<Comment> comments = taskService.getTaskComments(taskId);
        if (null != comments && comments.size() > 0) {
            BeanUtils.copyProperties(comments.get(0), actCommentVO);
            return actCommentVO;
        } else {
            return actCommentVO;
        }

    }

    @Override
    public List<ActCommentVO> queryCommentsByTaskId(String taskId) {
        // 1.根据任务id查询任务对象
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.从任务里面取出流程实例id
//        String processInstanceId = task.getProcessInstanceId();
        // 3.查询整个流程实例里面的所有批注信息
//        return this.queryCommentsByProcessInstanceId(processInstanceId);
        // 上面的方法在查询流程已结束的任务意见时会报空指针

        List<Comment> comments = taskService.getTaskComments(taskId);
        return this.transformComments(comments);
    }

    private List<ActCommentVO> transformComments(List<Comment> comments) {
        List<ActCommentVO> list = new ArrayList<>();
        Set<String> taskIdSet = new HashSet<>();
        if (comments != null) {
            for (Comment comment : comments) {
                String taskId = comment.getTaskId();
                if (taskIdSet.contains(taskId)) {
                    // 同一个任务的多个批注只取最新的
                    continue;
                }
                taskIdSet.add(taskId);
                ActCommentVO actCommentVO = new ActCommentVO();
                BeanUtils.copyProperties(comment, actCommentVO);
                list.add(actCommentVO);
            }
        }
        return list;
    }

    @Override
    public List<ActCommentVO> queryCommentsByProcessInstanceId(String processInstanceId) {
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        return this.transformComments(comments);
    }

    @Override
    public List<ActCommentVO> queryAllComments(String processInstanceId) {
        List<ActCommentVO> list = new ArrayList<>();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        if (comments != null) {
            for (Comment comment : comments) {
                ActCommentVO actCommentVO = new ActCommentVO();
                BeanUtils.copyProperties(comment, actCommentVO);
                list.add(actCommentVO);
            }
        }
        return list;
    }

    @Override
    public List<ActCommentVO> queryCommentsByBusinessKey(String businessKey) {
        List<ActCommentVO> result = new ArrayList<>();
        // 根据业务id查询历史流程实例
        List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (null != hpiList && hpiList.size() > 0) {
            for (HistoricProcessInstance historicProcessInstance : hpiList) {
                // 从历史流程实例取出实例id
                String processInstanceId = historicProcessInstance.getId();
                // 查询整个流程实例里面所有批注信息
                List<ActCommentVO> actCommentEntityList = this.queryCommentsByProcessInstanceId(processInstanceId);
                result.addAll(actCommentEntityList);
            }
        }
        return result;
    }

    @Override
    public List<ActCommentVO> queryCommentsProcessTypeAndByBusinessId(String processOperation, String processObject, Long businessId) {
        String businessKey = this.queryBusinessKey(processOperation, processObject, businessId);
        return this.queryCommentsByBusinessKey(businessKey);
    }

    @Override
    public PageVO<MyApplicationVO> queryMyApproved(Long userId, MyApplicationRequest request) throws ParseException {
        List<MyApplicationVO> allList = new ArrayList<>();

        List<String> taskIdList = this.workflowTaskService.queryApproved(userId);
        taskIdList = taskIdList == null ? new ArrayList<>() : taskIdList;
        Map<String, Object> params = this.transformToParams(request);
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        this.setTaskInfoQueryCondition(historicTaskInstanceQuery, params);
        historicTaskInstanceQuery.taskAssignee(userId.toString());
        List<HistoricTaskInstance> htiList = historicTaskInstanceQuery.orderByTaskCreateTime().desc().list();
        if (null != htiList && htiList.size() > 0) {
            for (HistoricTaskInstance historicTaskInstance : htiList) {
                if ("申请".equals(historicTaskInstance.getName())) {
                    continue;
                }
                MyApplicationVO myApplicationVO = new MyApplicationVO();
                BeanUtils.copyProperties(historicTaskInstance, myApplicationVO);
                allList.add(myApplicationVO);
                taskIdList.remove(myApplicationVO.getId());
            }
        }

        if (taskIdList.size() > 0) {
            for (String taskId : taskIdList) {
                HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
                this.setTaskInfoQueryCondition(query, params);
                query.taskId(taskId);
                List<HistoricTaskInstance> hList = query.orderByTaskCreateTime().desc().list();
                if (hList != null && hList.size() > 0 && !"申请".equals(hList.get(0).getName())) {
                    MyApplicationVO myApplicationVO = new MyApplicationVO();
                    BeanUtils.copyProperties(hList.get(0), myApplicationVO);
                    allList.add(myApplicationVO);
                }
            }
            allList.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
        }

        Integer pageNo = request.getPageNo() == null ? 1 : request.getPageNo();
        Integer pageSize = request.getPageSize() == null ? Constants.TWENTY : request.getPageSize();
        PageVO<MyApplicationVO> result = new PageVO<>(pageNo, pageSize, allList);
        Map<String, Map<String, Object>> variableMap = new HashMap<>();
        List<MyApplicationVO> data = result.getData();
        if (data != null && data.size() > 0) {
            for (MyApplicationVO vo : data) {
                String processInstanceId = vo.getProcessInstanceId();
                Map<String, Object> processVariables = variableMap.get(processInstanceId);
                if (processVariables == null) {
                    processVariables = this.getProcessVariables(processInstanceId);
                    variableMap.put(processInstanceId, processVariables);
                }
                vo.setProcessVariables(processVariables);
            }
        }

        return result;
    }

    @Override
    public List<ActHistoricTaskInstanceVO> queryHistoryTask(ActHistoricTaskInstanceEntity actHistoricTaskInstanceEntity,
                                                            Map<String, Object> processVariables,
                                                            Boolean finished,
                                                            Boolean processFinished,
                                                            boolean outcomeFlag) {
        // act_hi_taskinst
        List<ActHistoricTaskInstanceVO> list = new ArrayList<>();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        this.setTaskInfoQueryCondition(historicTaskInstanceQuery, processVariables);

        if (actHistoricTaskInstanceEntity != null) {
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getId())) {
                historicTaskInstanceQuery.taskId(actHistoricTaskInstanceEntity.getId());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getProcessDefinitionId())) {
                historicTaskInstanceQuery.processDefinitionId(actHistoricTaskInstanceEntity.getProcessDefinitionId());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getTaskDefinitionKey())) {
                historicTaskInstanceQuery.taskDefinitionKey(actHistoricTaskInstanceEntity.getTaskDefinitionKey());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getProcessInstanceId())) {
                historicTaskInstanceQuery.processInstanceId(actHistoricTaskInstanceEntity.getProcessInstanceId());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getExecutionId())) {
                historicTaskInstanceQuery.executionId(actHistoricTaskInstanceEntity.getExecutionId());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getName())) {
                historicTaskInstanceQuery.taskName(actHistoricTaskInstanceEntity.getName());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getParentTaskId())) {
                historicTaskInstanceQuery.taskParentTaskId(actHistoricTaskInstanceEntity.getParentTaskId());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getDescription())) {
                historicTaskInstanceQuery.taskDescription(actHistoricTaskInstanceEntity.getDescription());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getOwner())) {
                historicTaskInstanceQuery.taskOwner(actHistoricTaskInstanceEntity.getOwner());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getAssignee())) {
                historicTaskInstanceQuery.taskAssignee(actHistoricTaskInstanceEntity.getAssignee());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getDeleteReason())) {
                historicTaskInstanceQuery.taskDeleteReason(actHistoricTaskInstanceEntity.getDeleteReason());
            }
            if (null != actHistoricTaskInstanceEntity.getPriority()) {
                historicTaskInstanceQuery.taskPriority(actHistoricTaskInstanceEntity.getPriority());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getCategory())) {
                historicTaskInstanceQuery.taskCategory(actHistoricTaskInstanceEntity.getCategory());
            }
            if (StringUtils.isNotEmpty(actHistoricTaskInstanceEntity.getTenantId())) {
                historicTaskInstanceQuery.taskTenantId(actHistoricTaskInstanceEntity.getTenantId());
            }
        }

        if (finished != null) {
            if (finished) {
                historicTaskInstanceQuery.finished();
            } else {
                historicTaskInstanceQuery.unfinished();
            }

        }

        if (processFinished != null) {
            if (processFinished) {
                historicTaskInstanceQuery.processFinished();
            } else {
                historicTaskInstanceQuery.processUnfinished();
            }

        }

        List<HistoricTaskInstance> htiList = historicTaskInstanceQuery.orderByTaskCreateTime().desc().list();
        if (null != htiList && htiList.size() > 0) {
            for (HistoricTaskInstance historicTaskInstance : htiList) {
                ActHistoricTaskInstanceVO h = new ActHistoricTaskInstanceVO();
                BeanUtils.copyProperties(historicTaskInstance, h);
                if (outcomeFlag) {
                    String taskId = h.getId();
                    h.setOutcome(this.queryHistoryTaskOutcome(taskId));
                    ActCommentVO actCommentVO = this.queryCommentByTaskId(h.getId());
                    if (actCommentVO != null) {
                        h.setComment(actCommentVO.getMessage());
                    }
                }
                list.add(h);
            }
        }
        //SELECT d.TEXT_, t.* FROM ACT_HI_TASKINST t
        //LEFT JOIN `ACT_HI_ACTINST` a ON  a.TASK_ID_ = t.ID_
        //LEFT JOIN `ACT_HI_DETAIL` d ON a.ID_ = d.ACT_INST_ID_ AND d.NAME_ = 'outcome'
        //WHERE ...
        return list;
    }

    @Override
    public void setVariableByExecutionId(String executionId, String variableName, Object value) {
        // 流程变量 act_ru_variable act_hi_varinst
        runtimeService.setVariable(executionId, variableName, value);
//        Map<String, Object> variables = new HashMap<>();
//        runtimeService.setVariablesLocal(executionId, variables);//local的方法针对多线流程
    }

    @Override
    public String queryHistoryTaskOutcome(String taskId) {
        //SELECT d.TEXT_
        //FROM `ACT_HI_DETAIL` d
        //INNER JOIN `ACT_HI_ACTINST` a ON a.ID_ = d.ACT_INST_ID_
        //WHERE d.NAME_ = 'outcome' AND  a.TASK_ID_ = '15029'
        String outcome = null;
        String actInstSql = "select * from "
                + managementService.getTableName(HistoricActivityInstance.class)
                + " where TASK_ID_ = #{taskId} order by START_TIME_ desc";
        List<HistoricActivityInstance> actInstSqlList =
                historyService.createNativeHistoricActivityInstanceQuery().sql(actInstSql).parameter("taskId", taskId).list();
        if (actInstSqlList != null && actInstSqlList.size() > 0) {
            String actInstId = actInstSqlList.get(0).getId();
            List<HistoricDetail> detailList = historyService.createHistoricDetailQuery().activityInstanceId(actInstId).list();
            if (detailList != null && detailList.size() > 0) {
                for (HistoricDetail detail : detailList) {
                    HistoricDetailVariableInstanceUpdateEntity temp = (HistoricDetailVariableInstanceUpdateEntity) detail;
                    if ("outcome".equals(temp.getName())) {
                        outcome = temp.getTextValue();
                    }
                }
            }
        }
        return outcome;
    }

    @Override
    public Object getVariableByExecutionId(String executionId, String variableName) {
        return runtimeService.getVariable(executionId, variableName);
    }

    @Override
    public void setVariableByTaskId(String taskId, String variableName, Object value) {
        // 流程变量 act_ru_variable act_hi_varinst
        taskService.setVariable(taskId, variableName, value);
//        Map<String, Object> variables = new HashMap<>();
//        taskService.setVariablesLocal(taskId, variables);// local方法会把此任务id插入进数据库，标记哪个任务设置的变量
    }

    @Override
    public Object getVariableByTaskId(String taskId, String variableName) {
        return taskService.getVariable(taskId, variableName);
    }

    @Override
    public Map<String, Object> getVariablesByTaskId(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    public void getVariablesToList(List<ActTaskVO> taskVOList) {
        if (taskVOList != null && taskVOList.size() > 0) {
            for (ActTaskVO taskVO : taskVOList) {
                taskVO.setVariables(this.getVariablesByTaskId(taskVO.getId()));
            }
        }
    }

    @Override
    public Object getProcessVariable(String processInstanceId, String variableName) {
        HistoricVariableInstance historicVariableInstance = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(variableName)
                .singleResult();
        return historicVariableInstance.getValue();
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        List<HistoricVariableInstance> list = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
//                .variableName(variableName)
                .list();
        Map<String, Object> variables = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance variable : list) {
                variables.put(variable.getVariableName(), variable.getValue());
            }
        }
        return variables;
    }

    @Override
    public Map<String, Object> queryTaskCoordinateByTaskId(String taskId) {
        // 根据任务id查询任务节点坐标
        Map<String, Object> coordinate = new HashMap<>();
        // 1.根据任务id查询任务实例
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.从任务里面取出流程实例id
        String processInstanceId = task.getProcessInstanceId();
        // 3.根据流程实例id查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 4. 从流程实例对象取出当前活动节点id
        String activityId = processInstance.getActivityId(); // usertask1
        // 5.从任务里面取出流程定义id
        String processDefinitionId = task.getProcessDefinitionId();
        // 6.根据流程定义id查询流程定义的XML信息
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        // 7.使用活动id取出xml和当前活动id相关的节点数据
        ActivityImpl activityImpl = processDefinition.findActivity(activityId);
        // 8.从activityImpl取出坐标信息
        coordinate.put("x", activityImpl.getX());
        coordinate.put("y", activityImpl.getY());
        coordinate.put("width", activityImpl.getWidth());
        coordinate.put("height", activityImpl.getHeight());
        return coordinate;
    }

    @Override
    public List<Map<String, Object>> queryLineOutcomeByTaskId(String taskId) {
        // 根据任务id查询连线信息
        // 1.根据任务id查询任务实例
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.从任务里面取出流程实例id
        String processInstanceId = task.getProcessInstanceId();
        // 3.根据流程实例id查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 4. 从流程实例对象取出当前活动节点id
        String activityId = processInstance.getActivityId(); // usertask1
        // 5.从任务里面取出流程定义id
        String processDefinitionId = task.getProcessDefinitionId();
        // 6.根据流程定义id查询流程定义的XML信息
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        // 7.使用活动id取出xml和当前活动id相关的节点数据
        ActivityImpl activityImpl = processDefinition.findActivity(activityId);
        // 8.从activityImpl取出连线信息
        List<PvmTransition> transitions = activityImpl.getOutgoingTransitions();

        List<Map<String, Object>> list = new ArrayList<>();
        if (null != transitions && transitions.size() > 0) {
            if (transitions.size() == 1 && transitions.get(0).getProperty("conditionText") == null) {
                transitions = transitions.get(0).getDestination().getOutgoingTransitions();
                if (null == transitions || transitions.size() == 0) {
                    return list;
                }
            }

            // PvmTransition就是连线对象
            for (PvmTransition pvmTransition : transitions) {
                Map<String, Object> map = new HashMap<>();
                String name = String.valueOf(pvmTransition.getProperty("name"));
                map.put("name", name);
                Object conditionText = pvmTransition.getProperty("conditionText");
                map.put("conditionText", conditionText);
                if (conditionText != null) {
                    String[] arr = conditionText.toString().split("==");
                    if (arr.length > 1) {
                        String conditionValue = arr[1].trim();
                        conditionValue = conditionValue.startsWith("'") ? conditionValue.split("'")[1] : conditionValue;
                        conditionValue = conditionValue.startsWith("\"") ? conditionValue.split("\"")[1] : conditionValue;
                        map.put("conditionValue", conditionValue);
                    }
                }
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    public void deleteHistoricProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    public void queryHistoryProcessInstance() {
        // act_hi_actinst
        historyService.createHistoricProcessInstanceQuery()
//                .processDefinitionId()
//                .processDefinitionKey()
//                .processDefinitionName()
//                .processDefinitionVersion()
//                .processInstanceBusinessKey()
//                .processInstanceId()
//                .orderByProcessDefinitionId()
//                .orderByProcessInstanceBusinessKey()
//                .orderByProcessInstanceDuration()
//                .orderByProcessInstanceStartTime()
//                .orderByProcessInstanceId()
                .list();
    }

    public void queryHistoryAct() {
        historyService.createHistoricActivityInstanceQuery()
//                .activityId()
//                .activityInstanceId()
//                .activityName()
//                .taskAssignee("assignee")
//                .orderByActivityId()
//                .orderByActivityName()
                .list();
    }

    public void queryProcessInstance() {
        // act_ru_execution
        runtimeService.createProcessInstanceQuery().list();
    }


    /**
     * 获取未经完成的流程实例查询对象
     *
     * @param processDefKey 流程定义key
     * @return ProcessInstanceQuery
     */
    @Transactional(readOnly = true)
    public ProcessInstanceQuery createUnFinishedProcessInstanceQuery(String processDefKey) {
        ProcessInstanceQuery unfinishedQuery = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefKey)
                .active();
        return unfinishedQuery;
    }

    /**
     * 获取已经完成的流程实例查询对象
     *
     * @param processDefKey 流程定义key
     * @return HistoricProcessInstanceQuery
     */
    @Transactional(readOnly = true)
    public HistoricProcessInstanceQuery createFinishedProcessInstanceQuery(String processDefKey) {
        HistoricProcessInstanceQuery finishedQuery = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefKey).finished();
        return finishedQuery;
    }


    public void test() {
        // 动态指定任务办理人
        Map<String, Object> variables = new HashMap<String, Object>();
        String hrUserId = "1";
        variables.put("hrUserId", hrUserId);
        String taskId = "1";
        taskService.complete(taskId, variables);

        //下面的代码是利用initiator功能，设置一个名称（不是变量而是变量名）到启动事件上，并且在启动流程时调用一些下面的方法
        String currentUserId = "1";
        identityService.setAuthenticatedUserId(currentUserId);
        //引擎会记录启动人，即在ACT_HI_PROINST表的START_USER_ID字段，可以通过下面的代码获取。
        HistoricProcessInstance hi = historyService.createHistoricProcessInstanceQuery().singleResult();
        hi.getStartUserId();
    }
}

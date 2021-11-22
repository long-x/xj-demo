package com.ecdata.cmp.activiti.service;


import com.ecdata.cmp.activiti.dto.request.MyApplicationRequest;
import com.ecdata.cmp.activiti.dto.vo.ActCommentVO;
import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
import com.ecdata.cmp.activiti.dto.vo.ActProcessDefinitionVO;
import com.ecdata.cmp.activiti.dto.vo.ActTaskVO;
import com.ecdata.cmp.activiti.dto.vo.MyApplicationVO;
import com.ecdata.cmp.activiti.entity.ActDeploymentEntity;
import com.ecdata.cmp.activiti.entity.ActHistoricTaskInstanceEntity;
import com.ecdata.cmp.activiti.entity.ActProcessDefinitionEntity;
import com.ecdata.cmp.common.api.JSONObjectResponse;
import com.ecdata.cmp.common.dto.PageVO;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author xuxinsheng
 * @since 2020-01-06
 */
public interface IActivitiService {

    /**
     * 分隔符
     */
    String SEPARATOR = ":";
    /**
     * 默认流程定义key
     */
    String DEFAULT_PROCESS_DEFINITION_KEY = "workflow";

    /**
     * 部署流程
     *
     * @param deploymentName 部署名
     * @param zipPath        zip文件地址
     */
    void deployProcess(String deploymentName, String zipPath);

    /**
     * 部署流程
     *
     * @param deploymentName 部署名
     * @param zipInputStream zip流
     */
    void deployProcess(String deploymentName, ZipInputStream zipInputStream);

    /**
     * 流程部署
     *
     * @param deploymentName 部署名
     * @param bpmnPath       bpmn文件地址
     * @param pngPath        png图片地址
     */
    void deployProcess(String deploymentName, String bpmnPath, String pngPath);

    /**
     * 查询流程定义
     *
     * @param actProcessDefinitionEntity 流程定义参数
     * @return 流程定义列表
     */
    List<ActProcessDefinitionEntity> queryProcessDefinition(ActProcessDefinitionEntity actProcessDefinitionEntity);

    /**
     * 查询最新版本流程定义列表
     *
     * @return 最新版本流程定义列表
     */
    List<ActProcessDefinitionVO> queryLatestVersionProcessDefinition();

    /**
     * 根据部署名查询流程定义
     *
     * @param deploymentName 部署名
     * @return 流程定义列表
     */
    List<ActProcessDefinitionEntity> queryProcessDefinitionByDeploymentName(String deploymentName);

    /**
     * 更加任务id查询流程定义
     *
     * @param taskId 任务id
     * @return 流程定义
     */
    ActProcessDefinitionEntity queryProcessDefinitionByTaskId(String taskId);

    /**
     * 查询流程部署
     *
     * @param actDeploymentEntity 流程部署参数
     * @return 流程部署列表
     */
    List<ActDeploymentEntity> queryProcessDeployment(ActDeploymentEntity actDeploymentEntity);

    /**
     * 获取流程部署图片流
     *
     * @param deploymentId 部署id
     * @return 图片流
     */
    InputStream queryProcessDeploymentImage(String deploymentId);

    /**
     * 删除流程部署
     *
     * @param deploymentId 部署id
     * @param cascade      rue:级联删除正在执行的流程 act_ru_*和act_hi_*   false:如果有执行过流程，会报错
     */
    void deleteProcessDeploy(String deploymentId, boolean cascade);

    /**
     * 根据流程定义key删除流程部署
     *
     * @param processDefinitionKey 流程定义key
     * @param cascade              rue:级联删除正在执行的流程 act_ru_*和act_hi_*   false:如果有执行过流程，会报错
     */
    void deleteProcessDeployByProcessDefinitionKey(String processDefinitionKey, boolean cascade);

    /**
     * 开始流程
     *
     * @param processDefinitionKey 流程定义key
     * @param processOperation     自定义流程操作, 如:apply
     * @param processObject        自定义流程对象, 如:process
     * @param businessId           业务id
     * @param userId               用户id
     * @param params               流程变量参数
     * @return 流程实例id
     */
    String startProcess(String processDefinitionKey,
                        String processOperation,
                        String processObject,
                        Long businessId,
                        Long userId,
                        Map<String, Object> params);

    /**
     * 获取任务id
     *
     * @param processOperation 自定义流程操作, 如:apply
     * @param processObject    自定义流程对象, 如:process
     * @param businessId       业务id
     * @return 任务id
     */
    String queryTaskId(String processOperation, String processObject, Long businessId);


    /**
     * 查询代理人的任务(包括候选人已拾取的任务，即正在处理的任务)
     *
     * @param assignee  用户id
     * @param variables 条件变量
     * @return 任务列表
     */
    List<Task> queryAssigneeTask(String assignee, Map<String, Object> variables);


    /**
     * 查询候选人任务
     *
     * @param candidateUser 候选用户
     * @param variables     条件变量
     * @return 任务列表
     */
    List<Task> queryCandidateUserTask(String candidateUser, Map<String, Object> variables);

    /**
     * 查询候选组任务
     *
     * @param candidateGroups 候选组
     * @param variables       条件变量
     * @return 任务列表
     */
    List<Task> queryCandidateGroupTask(List<String> candidateGroups, Map<String, Object> variables);

    /**
     * 查询用户任务
     *
     * @param userId    用户id
     * @param groups    用户拥有权限组
     * @param variables 条件变量
     * @param backlogFlag true:级联查询自定义待办; false:不级联查询
     * @return 任务列表
     */
    List<ActTaskVO> queryUserTask(String userId, List<String> groups, Map<String, Object> variables, boolean backlogFlag);

    /**
     * 转换成参数
     *
     * @param request 请求
     * @return Map
     * @throws ParseException 解析异常
     */
    Map<String, Object> transformToParams(MyApplicationRequest request) throws ParseException;

    /**
     * 查询我的请求
     *
     * @param userId  用户id
     * @param request 请求参数
     * @param status  状态(1:待办;2:已办;)
     * @return PageVO<MyApplicationVO>
     * @throws ParseException 解析异常
     */
    PageVO<MyApplicationVO> queryMyRequest(Long userId, MyApplicationRequest request, Integer status) throws ParseException;

    /**
     * 查询运行中的任务
     *
     * @param processInstanceId    流程实例id
     * @param processVariablesFlag 流程变量查询标志(true:级联查询流程变量;false:不级联查询;)
     * @return List<ActTaskVO>
     */
    List<ActTaskVO> queryRunTask(String processInstanceId, boolean processVariablesFlag);

    /**
     * 任务拾取(签收)
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    void taskClaim(String taskId, Long userId);

    /**
     * 任务拾取(签收)回退
     *
     * @param taskId 任务id
     */
    void taskClaimBack(String taskId);

    /**
     * 处理任务代理人
     *
     * @param taskId 任务id
     * @param userId 用户id(若为空，则清除代理人)
     */
    void handleTaskAssignee(String taskId, Long userId);

    /**
     * 查询任务的组用户
     *
     * @param taskId 任务id
     * @return 用户列表
     */
    List<IdentityLink> queryGroupUser(String taskId);

    /**
     * 添加候选用户
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    void addCandidateUser(String taskId, String userId);

    /**
     * 添加候选组
     *
     * @param taskId  任务id
     * @param groupId 组id
     */
    void addCandidateGroup(String taskId, String groupId);

    /**
     * 删除候选用户
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    void deleteCandidateUser(String taskId, String userId);

    /**
     * 删除候选用户
     *
     * @param taskId  任务id
     * @param groupId 组id
     */
    void deleteCandidateGroup(String taskId, String groupId);

    /**
     * 完成任务
     *
     * @param taskId   任务id
     * @param outcome  连接名称
     * @param userId   用户id
     * @param userName 用户名称
     * @param comment  意见
     * @param params   流程变量参数
     * @return JSONObjectResponse
     */
    JSONObjectResponse completeTask(String taskId, String outcome, Long userId, String userName, String comment, Map<String, Object> params);

    /**
     * 根据任务id查询批注信息
     *
     * @param taskId 任务id
     * @return 批注信息
     */
    ActCommentVO queryCommentByTaskId(String taskId);

    /**
     * 根据任务id查询流程批注信息
     *
     * @param taskId 任务id
     * @return 批注信息列表
     */
    List<ActCommentVO> queryCommentsByTaskId(String taskId);

    /**
     * 根据流程实例id查询流程批注信息
     *
     * @param processInstanceId 流程实例id
     * @return 批注信息列表
     */
    List<ActCommentVO> queryCommentsByProcessInstanceId(String processInstanceId);

    /**
     * 根据流程实例id查询流程所有批注信息(不排除同一个任务id的多个批注)
     *
     * @param processInstanceId 流程实例id
     * @return 批注信息列表
     */
    List<ActCommentVO> queryAllComments(String processInstanceId);

    /**
     * 根据业务key查询流程批注信息
     *
     * @param businessKey 业务key
     * @return 批注信息列表
     */
    List<ActCommentVO> queryCommentsByBusinessKey(String businessKey);

    /**
     * 根据业务id和流程类型查询流程批注信息
     *
     * @param processOperation 自定义流程操作, 如:apply
     * @param processObject    自定义流程对象, 如:process
     * @param businessId       业务id
     * @return 批注信息列表
     */
    List<ActCommentVO> queryCommentsProcessTypeAndByBusinessId(String processOperation,
                                                               String processObject,
                                                               Long businessId);

    /**
     * 获取我的已办审批
     *
     * @param userId  用户id
     * @param request 请求参数
     * @return PageVO<MyApplicationVO>
     * @throws ParseException 解析异常
     */
    PageVO<MyApplicationVO> queryMyApproved(Long userId, MyApplicationRequest request) throws ParseException;

    /**
     * 获取历史任务
     *
     * @param actHistoricTaskInstanceEntity 历史任务参数
     * @param processVariables              流程变量,
     * @param finished                      任务结束
     * @param processFinished               流程结束
     * @param outcomeFlag                   true: 级联查询结果; false: 不级联查询
     * @return 历史任务列表
     */
    List<ActHistoricTaskInstanceVO> queryHistoryTask(ActHistoricTaskInstanceEntity actHistoricTaskInstanceEntity,
                                                     Map<String, Object> processVariables,
                                                     Boolean finished,
                                                     Boolean processFinished,
                                                     boolean outcomeFlag);

    /**
     * 获取历史任务流程的流出步骤
     *
     * @param taskId 任务id
     * @return 流程的流出步骤
     */
    String queryHistoryTaskOutcome(String taskId);

    /**
     * 通过执行id设置变量
     *
     * @param executionId  执行id
     * @param variableName 变量名
     * @param value        变量值
     */
    void setVariableByExecutionId(String executionId, String variableName, Object value);

    /**
     * 通过执行id获取变量
     *
     * @param executionId  执行id
     * @param variableName 变量名
     * @return 变量值
     */
    Object getVariableByExecutionId(String executionId, String variableName);

    /**
     * 通过任务id设置变量
     *
     * @param taskId       任务id
     * @param variableName 变量名
     * @param value        变量值
     */
    void setVariableByTaskId(String taskId, String variableName, Object value);

    /**
     * 通过任务id获取变量
     *
     * @param taskId       任务id
     * @param variableName 变量名
     * @return 变量值
     */
    Object getVariableByTaskId(String taskId, String variableName);

    /**
     * 通过任务id获取变量
     *
     * @param taskId 任务id
     * @return 变量
     */
    Map<String, Object> getVariablesByTaskId(String taskId);

    /**
     * 获取流程变量并设置到列表中
     *
     * @param taskVOList act任务vo列表
     */
    void getVariablesToList(List<ActTaskVO> taskVOList);

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例id
     * @param variableName      变量名
     * @return 变量值
     */
    Object getProcessVariable(String processInstanceId, String variableName);

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例id
     * @return 变量值
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * 根据任务id查询任务节点坐标
     *
     * @param taskId 任务id
     * @return 任务节点坐标
     */
    Map<String, Object> queryTaskCoordinateByTaskId(String taskId);

    /**
     * 根据任务id查询连线信息
     *
     * @param taskId 任务id
     * @return 连线信息
     */
    List<Map<String, Object>> queryLineOutcomeByTaskId(String taskId);

    /**
     * 删除正在运行中的流程实例
     *
     * @param processInstanceId 流程实例id
     * @param deleteReason      删除理由
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 删除历史流程实例
     *
     * @param processInstanceId 流程实例id
     */
    void deleteHistoricProcessInstance(String processInstanceId);
}

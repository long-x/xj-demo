package com.ecdata.cmp.activiti.actlistener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.activiti.entity.WorkflowStep;
import com.ecdata.cmp.activiti.entity.WorkflowStepRight;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;
import com.ecdata.cmp.activiti.service.IActivitiService;
import com.ecdata.cmp.activiti.service.IWorkflowStepRightService;
import com.ecdata.cmp.activiti.service.IWorkflowStepService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskCandidateService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskService;
import com.ecdata.cmp.activiti.utils.SpringContextUtil;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.user.client.RoleClient;
import com.ecdata.cmp.user.client.SysNotificationClient;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Slf4j
public class WorkflowListener implements TaskListener {

    private static final long serialVersionUID = 5999223902881365346L;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<String> groups = new ArrayList<>();
        List<String> users = new ArrayList<>();
        String assignee = null;

        Map<String, Object> variables = delegateTask.getVariables();
        String userId = String.valueOf(variables.get("userId"));
        String outcome = (String) variables.get("outcome");
        Integer processWorkflowStep = (Integer) variables.get("processWorkflowStep");
        String notifyMessage = (String) variables.get("notifyMessage");
        String notifyDetail = (String) variables.get("notifyDetail");
        if (StringUtils.isEmpty(notifyMessage)) {
            notifyMessage = "申请流程";
        }
        if (StringUtils.isEmpty(notifyDetail)) {
            notifyDetail = "申请流程";
        }
        if (StringUtils.isEmpty(outcome)) {
            // 开始流程不做处理
            return;
        } else if ("驳回".equals(outcome)) {
            notifyMessage = "驳回: " + notifyMessage;
            notifyDetail = "驳回: " + notifyDetail;
            assignee = userId;
            processWorkflowStep = 1;
            delegateTask.setVariable("processWorkflowStep", processWorkflowStep);
        } else {
            IWorkflowStepService stepService = SpringContextUtil.getBean(IWorkflowStepService.class);
            Long processWorkflowId = (Long) variables.get("processWorkflowId");
            WorkflowStep workflowStep = null;
            Integer tempSort = processWorkflowStep == null ? 1 : processWorkflowStep;
            if ("同意".equals(outcome) || "提交".equals(outcome)) {
                workflowStep = stepService.getNextWorkflowStep(processWorkflowId, processWorkflowStep);
                tempSort++;
            } else if ("拒绝".equals(outcome)) {
                workflowStep = stepService.getBackWorkflowStep(processWorkflowId, processWorkflowStep);
                tempSort--;
            }

            if (workflowStep != null) {
                IWorkflowStepRightService rightService = SpringContextUtil.getBean(IWorkflowStepRightService.class);
                List<WorkflowStepRight> rightList = rightService.getWorkflowStepRight(workflowStep.getId());
                if (rightList != null && rightList.size() > 0) {
                    for (WorkflowStepRight right : rightList) {
                        String relateId = String.valueOf(right.getRelateId());
                        Integer type = right.getType();
                        if (type != null && type == 1) {
                            users.add(relateId);
                        }else if (type != null && type == 6) {
                            users.add(userId);
                        } else {
                            groups.add(type + IActivitiService.SEPARATOR + relateId);
                        }
                    }
                } else {
                    this.setAdminCandidate(groups, users, userId);
                }
                processWorkflowStep = workflowStep.getSort();
            } else {
                this.setAdminCandidate(groups, users, userId);
                processWorkflowStep = tempSort;
            }
            delegateTask.setVariable("processWorkflowStep", processWorkflowStep);
        }

        // 自定义候选人情况
        IWorkflowTaskService taskService = SpringContextUtil.getBean(IWorkflowTaskService.class);
        QueryWrapper<WorkflowTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.lambda().eq(WorkflowTask::getProcessInstanceId, delegateTask.getProcessInstanceId())
                .eq(WorkflowTask::getWorkflowStep, processWorkflowStep);
        List<WorkflowTask> taskList = taskService.list(taskQueryWrapper);
        if (taskList != null && taskList.size() > 0) {
            IWorkflowTaskCandidateService candidateService = SpringContextUtil.getBean(IWorkflowTaskCandidateService.class);
            WorkflowTask task = taskList.get(0);
            List<WorkflowTaskCandidate> candidateList = candidateService.listCandidate(task.getId(), null);
            if (candidateList != null && candidateList.size() > 0) {
                task.setTaskId(null).setIsApproved(0);
                taskService.updateById(task);
                users.clear();
                groups.clear();
                for (WorkflowTaskCandidate candidate : candidateList) {
                    String relateId = String.valueOf(candidate.getRelateId());
                    Integer type = candidate.getType();
                    if (type != null && type == 1) {
                        users.add(relateId);
                    } else {
                        groups.add(type + IActivitiService.SEPARATOR + relateId);
                    }
                    candidate.setIsApproved(0);
                }
                taskService.updateBatchById(taskList);
            }
        }

        if (users.size() > 0) {
            delegateTask.addCandidateUsers(users);
        }
        if (groups.size() > 0) {
            delegateTask.addCandidateGroups(groups);
        }
        if (StringUtils.isNotEmpty(assignee)) {
            delegateTask.setAssignee(assignee);
            users.add(assignee);
        }
//        this.notifyMessage(userId, groups, users, notifyMessage, notifyDetail);
    }

    private void setAdminCandidate(List<String> groups, List<String> users, String userId) {
        RoleClient roleClient = SpringContextUtil.getBean(RoleClient.class);
        try {
            List<RoleVO> roleVOList = roleClient.listAdmin(AuthContext.getAuthz()).getData();
            if (roleVOList != null && roleVOList.size() > 0) {
                for (RoleVO roleVO : roleVOList) {
                    groups.add(Constants.TWO + IActivitiService.SEPARATOR + roleVO.getId());
                }
            } else {
                users.add(userId);
            }
        } catch (Exception e) {
            log.info("获取管理员角色异常");
            users.add(userId);
        }
    }

    private void notifyMessage(String userId, List<String> groups, List<String> users, String notifyMessage, String notifyDetail) {
        SysNotificationClient sysNotificationClient = SpringContextUtil.getBean(SysNotificationClient.class);
        List<Long> userIdList = new ArrayList<>();
        List<Long> roleIdList = new ArrayList<>();
        List<Long> departmentIdList = new ArrayList<>();
        List<Long> projectIdList = new ArrayList<>();
        List<Long> businessGroupIdList = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (String user : users) {
                if (StringUtils.isNumeric(user)) {
                    userIdList.add(Long.valueOf(user));
                }
            }
        }
        if (groups != null && groups.size() > 0) {
            for (String group : groups) {
                if (StringUtils.isNotEmpty(group)) {
                    String[] info = group.split(IActivitiService.SEPARATOR);
                    if (!StringUtils.isNumeric(info[1])) {
                        continue;
                    }
                    Long id = Long.valueOf(info[1]);
                    switch (info[0]) {
                        case "1":
                            if (!userIdList.contains(id)) {
                                userIdList.add(id);
                            }
                            break;
                        case "2":
                            roleIdList.add(id);
                            break;
                        case "3":
                            departmentIdList.add(id);
                            break;
                        case "4":
                            projectIdList.add(id);
                            break;
                        case "5":
                            businessGroupIdList.add(id);
                            break;
                        default:
                            log.info("不识别类型：" + info[0]);
                            break;
                    }
                }
            }
        }
        SysNotificationRequest sysNotificationRequest = new SysNotificationRequest();
        sysNotificationRequest.setType(Constants.ONE);
        sysNotificationRequest.setSourceType(Constants.FOUR);
        sysNotificationRequest.setUserIds(userIdList);
        sysNotificationRequest.setRoleIds(roleIdList);
        sysNotificationRequest.setDepartmentIds(departmentIdList);
        sysNotificationRequest.setProjectIds(projectIdList);
        sysNotificationRequest.setGroupIds(businessGroupIdList);
        sysNotificationRequest.setMessage(notifyMessage);
        sysNotificationRequest.setDetail(notifyDetail);
        sysNotificationRequest.setCreateUser(Long.valueOf(userId));
        String token = AuthContext.getAuthz();
        sysNotificationClient.addNotifToUserByType(token, sysNotificationRequest);
    }
}

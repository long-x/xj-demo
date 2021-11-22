package com.ecdata.cmp.activiti.service.impl;

import com.ecdata.cmp.activiti.service.WebServiceService;
import com.ecdata.cmp.activiti.webService.WorkflowRequestInfo;
import com.ecdata.cmp.activiti.webService.WorkflowService;
import com.ecdata.cmp.activiti.webService.WorkflowServiceLocator;
import com.ecdata.cmp.activiti.webService.WorkflowServicePortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xuxiaojian
 * @date 2020/4/13 9:57
 */
@Service
@Slf4j
public class WebServiceServiceImpl implements WebServiceService {

    @Override
    public String getUserId(String filedType, String filedValue) {
        try {
            WorkflowService workflowService = new WorkflowServiceLocator();

            WorkflowServicePortType workflowServiceHttpPort = workflowService.getWorkflowServiceHttpPort();
            log.info(
            workflowService.getWorkflowServiceHttpPortAddress());

            return workflowServiceHttpPort.getUserId(filedType, filedValue);
        } catch (Exception e) {
            log.error("获取人员ID异常，", e);
            return "";
        }
    }

    @Override
    public String doCreateWorkflowRequest(WorkflowRequestInfo workflowRequestInfo, Integer userId) {
        try {
            WorkflowService workflowService = new WorkflowServiceLocator();

            WorkflowServicePortType workflowServiceHttpPort = workflowService.getWorkflowServiceHttpPort();

            return workflowServiceHttpPort.doCreateWorkflowRequest(workflowRequestInfo, userId);
        } catch (Exception e) {
            log.error("创建工作流程异常，", e);
            return "";
        }
    }

    @Override
    public String submitWorkflowRequest(WorkflowRequestInfo workflowRequestInfo, Integer requestId, Integer userId, String type, String remark) {
        try {
            WorkflowService workflowService = new WorkflowServiceLocator();

            WorkflowServicePortType workflowServiceHttpPort = workflowService.getWorkflowServiceHttpPort();

            return workflowServiceHttpPort.submitWorkflowRequest(workflowRequestInfo, requestId, userId, type, remark);
        } catch (Exception e) {
            log.error("提交工作流程异常，", e);
            return "";
        }
    }
}

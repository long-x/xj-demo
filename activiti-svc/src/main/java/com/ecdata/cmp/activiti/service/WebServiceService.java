package com.ecdata.cmp.activiti.service;

import com.ecdata.cmp.activiti.webService.WorkflowRequestInfo;

/**
 * @author xuxiaojian
 * @date 2020/4/13 9:57
 */
public interface WebServiceService {
    String getUserId(String filedType, String filedValue);

    String doCreateWorkflowRequest(WorkflowRequestInfo workflowRequestInfo,Integer userId);

    String submitWorkflowRequest(WorkflowRequestInfo workflowRequestInfo, Integer requestId, Integer userId, String type, String remark);
}

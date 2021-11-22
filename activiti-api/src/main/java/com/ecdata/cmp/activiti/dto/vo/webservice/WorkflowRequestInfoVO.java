package com.ecdata.cmp.activiti.dto.vo.webservice;

import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/13 10:20
 */
@Data
public class WorkflowRequestInfoVO {
    private Integer userId;

    private String creatorId;

    private String requestLevel;

    private String requestName;

    private Integer requestId;//	流程请求ID
    private String type;//	提交类型：submit 提交 subnoback提交不需回复  subback提交需要回复 reject退回
    private String remark;//	签字意见

    private WorkflowBaseInfoVO workflowBaseInfo;

    private WorkflowMainTableInfoVO workflowMainTableInfo;
}

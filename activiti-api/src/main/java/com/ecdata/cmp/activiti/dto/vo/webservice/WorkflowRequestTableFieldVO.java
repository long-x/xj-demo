package com.ecdata.cmp.activiti.dto.vo.webservice;

import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/13 10:45
 */
@Data
public class WorkflowRequestTableFieldVO {
    private String fieldName;

    private String fieldType;

    private String fieldValue;

    private Boolean edit;

    private Boolean view;
}

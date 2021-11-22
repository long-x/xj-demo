package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:10
 */
@Data
public class IaasNextApplyUserParam {
    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("工作流id")
    private Long workflowId;

    @ApiModelProperty("工作流步骤")
    private Integer workflowStep;

    @ApiModelProperty("关联ID")
    private Long relateId;

    @ApiModelProperty("关联名")
    private String relateName;
}

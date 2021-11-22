package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:10
 */
@Data
public class IaasApplyRelationInfoParams {
    @ApiModelProperty(value = "申请id")
    private Long applyId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("工作流id")
    private Long workflowId;

    @ApiModelProperty("工作流步骤")
    private Integer workflowStep;

    @ApiModelProperty(value = "关联关系")
    private List<IaasApplyRelationInfoParam> paramList;
}

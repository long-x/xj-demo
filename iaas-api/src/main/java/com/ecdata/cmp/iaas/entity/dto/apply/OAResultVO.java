package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/13 17:26
 */
@Data
public class OAResultVO {
    @ApiModelProperty(value = "工作流程id")
    private String workflowId;

    @ApiModelProperty(value = "审批结果")
    private String approvalResult;

    @ApiModelProperty(value = "审批意见")
    private String approvalOpinion;
}

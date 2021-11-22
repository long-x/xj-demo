package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xuxiaojian
 * @date 2020/3/13 11:15
 */
@Data
@Accessors(chain = true)
public class IaasApplyAuditVO {
    @ApiModelProperty(value = "类型")
    private int type;

    @ApiModelProperty(value = "申请id")
    private Long applyId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "关联用户")
    private IaasApplyRelationInfoParam relationInfoParam;

    @ApiModelProperty(value = "计算资源", required = false)
    private IaasApplyCalculateVO iaasApplyCalculateVO;

    @ApiModelProperty(value = "安全服务信息", required = false)
    private IaasApplySecurityServerVO iaasApplySecurityServerVO;

    @ApiModelProperty(value = "存储信息", required = false)
    private IaasApplyStorageVO iaasApplyStorageVO;

    @ApiModelProperty(value = "软件服务", required = false)
    private IaasApplySoftwareServerVO iaasApplySoftwareServerVO;

    @ApiModelProperty(value = "网络策略信息", required = false)
    private IaasApplyNetworkPolicyVO iaasApplyNetworkPolicyVO;

    @ApiModelProperty(value = "其他信息", required = false)
    private IaasApplyOtherVO iaasApplyOtherVO;
}

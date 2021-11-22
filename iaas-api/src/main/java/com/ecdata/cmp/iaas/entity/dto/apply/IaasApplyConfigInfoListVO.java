package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/3 14:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "配置信息", description = "配置信息")
public class IaasApplyConfigInfoListVO {
    @ApiModelProperty(value = "审批完成")
    private List<IaasApplyConfigInfoVO> approvalEdConfigInfoList;
    @ApiModelProperty(value = "审批")
    private List<IaasApplyConfigInfoVO> inApprovalConfigInfoList;
    @ApiModelProperty(value = "草稿")
    private List<IaasApplyConfigInfoVO> configInfoList;
}

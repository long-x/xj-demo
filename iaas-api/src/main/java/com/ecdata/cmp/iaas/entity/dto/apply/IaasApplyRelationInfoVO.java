package com.ecdata.cmp.iaas.entity.dto.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xuxiaojian
 * @date 2020/4/14 14:33
 */
@Data
@ApiModel(value = "审核关系维护", description = "审核关系维护")
public class IaasApplyRelationInfoVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务id")
    private Long businessId;

    @ApiModelProperty(value = "申请id")
    private Long applyId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("关联用户")
    private Long relationUser;

    @ApiModelProperty("关联用户")
    private String relationUserName;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;
}

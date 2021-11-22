package com.ecdata.cmp.iaas.entity.dto.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xuxiaojian
 * @date 2020/3/26 14:51
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "审批其他信息", description = "审批其他信息")
public class IaasApplyOtherVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("申请id")
    private Long applyId;

    @ApiModelProperty("所在区域")
    private Long areaId;

    @ApiModelProperty("配置id")
    private Long configId;

    @ApiModelProperty("镜像id")
    private String imageId;

    @ApiModelProperty("镜像名称")
    private String imageName;

    @ApiModelProperty("镜像版本")
    private String imageVersion;

    @ApiModelProperty("私有云id")
    private String vpcId;

    @ApiModelProperty("私有云名称")
    private String vpcName;

    @ApiModelProperty("网卡id")
    private String networkId;

    @ApiModelProperty("网卡名称")
    private String networkName;

    @ApiModelProperty("子网id")
    private String subnetId;

    @ApiModelProperty("子网名称")
    private String subnetName;

    @ApiModelProperty("安全组id")
    private String securityGroupsId;

    @ApiModelProperty("安全组名称")
    private String securityGroupsName;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;
}

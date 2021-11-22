package com.ecdata.cmp.iaas.entity.apply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 其他信息表
 *
 * @author xxj
 * @date 2020-03-10 13:50:16
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_apply_other")
@ApiModel(value = "其他信息表", description = "其他信息表")
public class IaasApplyOther extends Model<IaasApplyOther> {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
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

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

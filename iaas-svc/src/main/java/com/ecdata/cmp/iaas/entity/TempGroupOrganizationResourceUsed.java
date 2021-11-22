package com.ecdata.cmp.iaas.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("temp_group_organization_resource_used")
@ApiModel(value = " 集团各组织资源使用情况临时表 对象", description = " 集团各组织资源使用情况临时表 对象")
public class TempGroupOrganizationResourceUsed extends Model<TempGroupOrganizationResourceUsed> {
    private static final long serialVersionUID = -5803503218101617296L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String organizationName;

    /**
     * 裸金属使用数量
     */
    @ApiModelProperty(value = "裸金属使用数量")
    private Integer bareMetalUsed;

    /**
     * 裸金属使用率
     */
    @ApiModelProperty(value = "裸金属使用率")
    private Double bareMetalUsageRate;

    /**
     * 虚拟机使用数量
     */
    @ApiModelProperty(value = "虚拟机使用数量")
    private Integer virtualMachineUsed;

    /**
     * 虚拟机使用率
     */
    @ApiModelProperty(value = "虚拟机使用率")
    private Double virtualMachinelUsageRate;

    /**
     * 存储使用数量
     */
    @ApiModelProperty(value = "存储使用数量")
    private Integer storageUsed;

    /**
     * 存储使用率
     */
    @ApiModelProperty(value = "存储使用率")
    private Double storageUsageRate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
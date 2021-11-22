package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value = "整体资源临时表 拓展类对象", description = "整体资源临时表 拓展类对象")
public class TempGroupOrganizationResourceUsedVO implements Serializable {
    private static final long serialVersionUID = -826748970889663341L;
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

}
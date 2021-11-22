package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: ResourcePoolDatastoreVO
 * @Author: shig
 * @description: 资源池存储 拓展类对象
 * @Date: 2019/11/19 9:40 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源池存储 拓展类对象", description = "资源池存储 拓展类对象")
public class ResourcePoolDatastoreVO implements Serializable {
    private static final long serialVersionUID = 5406869073999430268L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 资源池id
     */
    @ApiModelProperty(value = "资源池id")
    private Long poolId;

    /**
     * 资源存储id
     */
    @ApiModelProperty(value = "资源存储id")
    private String poolDatastoreId;

    /**
     * 分配存储总量(gb)
     */
    @ApiModelProperty(value = "分配存储总量(gb)")
    private Double spaceTotalAllocate;

    /**
     * 已用分配存储总量(gb)
     */
    @ApiModelProperty(value = "已用分配存储总量(gb)")
    private Double spaceUsedAllocate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;


    //其他对象属性
    /**
     * 存储:主机存储
     */
    @ApiModelProperty(value = "存储类型")
    private String datastoreName;

    @ApiModelProperty(value = "总空间")
    private String driveType;

    @ApiModelProperty(value = "已使用空间")
    private String spaceTotal;

    @ApiModelProperty(value = "已置备空间")
    private String spaceUsed;

    @ApiModelProperty(value = "已置备空间（超分比）")
    private String spaceRandio;

}
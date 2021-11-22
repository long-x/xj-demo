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

/**
 * @title: TempCloudDataCenter
 * @Author: shig
 * @description: TempCloudDataCenter
 * @Date: 2019/12/11 12:50 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "云数据中心临时表 拓展类对象", description = "云数据中心临时表 拓展类对象")
public class TempCloudDataCenterVO implements Serializable {

    private static final long serialVersionUID = 4123188618870605927L;

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
     * 云数据中心名称
     */
    @ApiModelProperty(value = "云数据中心名称")
    private String cdcName;

    /**
     * cpu使用百分比
     */
    @ApiModelProperty(value = "cpu使用百分比")
    private Double cpuRate;

    /**
     * cpu总量
     */
    @ApiModelProperty(value = "cpu总量")
    private Double cpuTotal;

    /**
     * cpu已用
     */
    @ApiModelProperty(value = "cpu已用")
    private Double cpuUsed;

    /**
     * 内存使用百分比
     */
    @ApiModelProperty(value = "内存使用百分比")
    private Double memoryRate;

    /**
     * 内存总量
     */
    @ApiModelProperty(value = "内存总量")
    private Double memoryTotal;

    /**
     * 内存已用
     */
    @ApiModelProperty(value = "内存已用")
    private Double memoryUsed;

    /**
     * 存储使用百分比
     */
    @ApiModelProperty(value = "存储使用百分比")
    private Double storageRate;

    /**
     * 存储总量
     */
    @ApiModelProperty(value = "存储总量")
    private Double storageTotal;

    /**
     * 存储已用
     */
    @ApiModelProperty(value = "存储已用")
    private Double storageUsed;

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
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = " 是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;


}
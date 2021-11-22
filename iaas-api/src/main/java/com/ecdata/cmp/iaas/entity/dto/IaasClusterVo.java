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
 * @title: IaasClusterVo
 * @Author: shig
 * @description: 集群 拓展类
 * @Date: 2019/11/19 10:01 上午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "集群 拓展类对象", description = "集群 拓展类对象")
public class IaasClusterVo implements Serializable {
    private static final long serialVersionUID = 6006313189657618041L;
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
     * 集群名
     */
    @ApiModelProperty(value = "集群名")
    private String clusterName;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /**
     * 虚拟机总量
     */
    @ApiModelProperty(value = "虚拟机总量")
    private Integer vmNum;

    /**
     * 总cpu频率
     */
    @ApiModelProperty(value = "总cpu频率")
    private Double cpuTotal;

    /**
     * 已用cpu频率
     */
    @ApiModelProperty(value = "已用cpu频率")
    private Double cpuUsed;

    /**
     * 磁盘总量
     */
    @ApiModelProperty(value = "磁盘总量")
    private Double diskTotal;

    /**
     * 已用磁盘总量
     */
    @ApiModelProperty(value = "已用磁盘总量")
    private Double diskUsed;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryTotal;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryUsed;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer score;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "关联唯一key")
    private String clusterKey;

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


    //其他字段
    @ApiModelProperty(value = "供应商id")
    private Long providerId;
    @ApiModelProperty(value = "资源池id")
    private Long poolId;
    @ApiModelProperty(value = "已分配cCPU数量")
    private Long hostCpuTotal;
    @ApiModelProperty(value = "已分配cCPU数量")
    private Long vcpuTotalAllocate;
    @ApiModelProperty(value = "已分配cCPU数量(超分比")
    private Long cpuTTHostRatio;
    @ApiModelProperty(value = "物理内存总数量")
    private Long hostMemoryTotal;
    @ApiModelProperty(value = "已分配内存总数量")
    private Long memoryTotalAllocate;
    @ApiModelProperty(value = "已分配内存总数量(超分比)")
    private Long vmTTHostRatio;
    @ApiModelProperty(value = "虚机数量")
    private Long vmTotalAllocate;

}

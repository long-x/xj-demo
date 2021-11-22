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
 * @title: VirtualMachineMonitor
 * @Author: shig
 * @description: 虚拟监控拓展
 * @Date: 2019/11/25 4:46 下午
 */

@Data
@Accessors(chain = true)
@ApiModel(value = "虚拟监控拓展", description = "虚拟监控拓展")
public class IaasVirtualMachineMonitorVO implements Serializable {

    private static final long serialVersionUID = 681898355342175051L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 虚拟机id
     */
    @ApiModelProperty(value = "虚拟机id")
    private Long vmId;

    @ApiModelProperty(value = "主机id")
    private Long hostId;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "集群key")
    private String clusterKey;

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
     * CPU使用率
     */
    @ApiModelProperty(value = "CPU使用率")
    private Double cpuUsageRate;

    /**
     * 总cpu核数
     */
    @ApiModelProperty(value = "总cpu核数")
    private double vcpuTotal;

    /**
     * 已用cpu核数
     */
    @ApiModelProperty(value = "已用cpu核数")
    private double vcpuUsed;

    /**
     * CPU核使用率
     */
    @ApiModelProperty(value = "CPU核使用率")
    private Double vcpuUsageRate;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private double memoryTotal;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private double memoryUsed;

    /**
     * 内存使用率
     */
    @ApiModelProperty(value = "内存使用率")
    private Double memoryUsageRate;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double diskTotal;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double diskUsed;

    /**
     * 内存使用率
     */
    @ApiModelProperty(value = "内存使用率")
    private Double diskUsageRate;

    /**
     * 网络流入速率(单位:KB/S)
     */
    @ApiModelProperty(value = "网络流入速率(单位:KB/S)")
    private Double nicByteIn;

    /**
     * 网络流出速率(单位:KB/S)
     */
    @ApiModelProperty(value = "网络流出速率(单位:KB/S)")
    private Double nicByteOut;

    /**
     * 磁盘I/O写入速率(单位:KB/S)
     */
    @ApiModelProperty(value = "磁盘I/O写入速率(单位:KB/S)")
    private Double diskIoOut;

    /**
     * 磁盘I/O读出速率(单位:KB/S)
     */
    @ApiModelProperty(value = "磁盘I/O读出速率(单位:KB/S)")
    private Double diskIoIn;

    /**
     * CPU等待时间(单位:ms)
     */
    @ApiModelProperty(value = "CPU等待时间(单位:ms)")
    private Long cpuReadyTime;

    /**
     * 磁盘I/O读命令次数(单位:次/S)
     */
    @ApiModelProperty(value = "磁盘I/O读命令次数(单位:次/S)")
    private Integer diskOutPs;

    /**
     * 磁盘I/O写命令次数(单位:次/S)
     */
    @ApiModelProperty(value = "磁盘I/O写命令次数(单位:次/S)")
    private Integer diskInPs;

    /**
     * 磁盘读时延(单位:ms)
     */
    @ApiModelProperty(value = "磁盘读时延(单位:ms)")
    private Integer diskReadDelay;

    /**
     * 磁盘写时延(单位:ms)
     */
    @ApiModelProperty(value = " 磁盘写时延(单位:ms)")
    private Integer diskWriteDelay;

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
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    @ApiModelProperty(value = "类型(1:虚拟机,2:集群,3:主机)")
    private Long type;

    /**
     * 其他属性
     */
    @ApiModelProperty(value = "主机名称")
    private String hostName;
    @ApiModelProperty(value = "集群id")
    private Long clusterId;
    @ApiModelProperty(value = "集群名称")
    private String clusterName;
    @ApiModelProperty(value = "时间范围")
    private String timeRange;
    @ApiModelProperty(value = "使用率类型(1:cpu,2:内存)")
    private String usageRate;
    @ApiModelProperty(value = "率类型(1:0-20,2:20-40,3:40-60,4:60-80,5:80-100)")
    private String radioType;

    @ApiModelProperty(value = "虚拟机数")
    private Double count;
    @ApiModelProperty(value = "虚拟机数(0-20%)")
    private Double count1;
    @ApiModelProperty(value = "虚拟机数(20-40%)")
    private Double count2;
    @ApiModelProperty(value = "虚拟机数(40-60%)")
    private Double count3;
    @ApiModelProperty(value = "虚拟机数(60-80%)")
    private Double count4;
    @ApiModelProperty(value = "虚拟机数(80-100%)")
    private Double count5;

    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    @ApiModelProperty(value = "日期String")
    private String createTimeStr;

}
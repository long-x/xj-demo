package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 14:25
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "宿主机利用率返回对象", description = "宿主机利用率返回对象")
public class PhysicalHostVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "ownerType")
    private String ownerType;

    @ApiModelProperty(value = "allocatedDisk")
    private String allocatedDisk;

    @ApiModelProperty(value = "ownerId")
    private String ownerId;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "核数")
    private String freeVcpuCores;

    @ApiModelProperty(value = "总磁盘")
    private String totalDisk;

    @ApiModelProperty(value = "分配的CPU")
    private String allocatedCpu;

    @ApiModelProperty(value = "总CPU")
    private String totalCpu;

    @ApiModelProperty(value = "确认状态")
    private String confirmStatus;

    @ApiModelProperty(value = "已分配CPU容量（MB）")
    private String allocatedVcpuCores;

    @ApiModelProperty(value = "已分配内存容量（MB）")
    private String allocatedMemory;

    @ApiModelProperty(value = "vMemory容量（MB）")
    private String totalVmemoryMB;

    @ApiModelProperty(value = "使用的CPU")
    private String usedCpu;

    @ApiModelProperty(value = "使用的内存")
    private String usedMemory;

    @ApiModelProperty(value = "序列号")
    private String serialNumber;

    @ApiModelProperty(value = "classId")
    private String classId;

    @ApiModelProperty(value = "BMC IP地址")
    private String bmcIp;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "使用的内存")
    private String usedDisk;

    @ApiModelProperty(value = "虚拟化类型")
    private String hypervisorType;

    @ApiModelProperty(value = "resId")
    private String resId;

    @ApiModelProperty(value = "已分配磁盘大小（MB）")
    private String allocatedDiskSizeMB;

    @ApiModelProperty(value = "已分配vMemory容量（MB）")
    private String allocatedVmemoryMB;

    @ApiModelProperty(value = "className")
    private String className;

    @ApiModelProperty(value = "azoneId")
    private String azoneId;

    @ApiModelProperty(value = "resourcePoolId")
    private String resourcePoolId;

    @ApiModelProperty(value = "totalVcpuCores")
    private String totalVcpuCores;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "isVirtual")
    private boolean isVirtual;

    @ApiModelProperty(value = "status")
    private String status;

    @ApiModelProperty(value = "logicalRegionName")
    private String logicalRegionName;

    @ApiModelProperty(value = "区域/集群名")
    private String azoneName;

    @ApiModelProperty(value = "regionName")
    private String regionName;

    @ApiModelProperty(value = "freeVmemoryMB")
    private String freeVmemoryMB;

    @ApiModelProperty(value = "cpuQuantityForVirtualization")
    private String cpuQuantityForVirtualization;

    @ApiModelProperty(value = "hypervisorEnable")
    private boolean hypervisorEnable;

    @ApiModelProperty(value = "ownerName")
    private String ownerName;

    @ApiModelProperty(value = "allocatedRamSizeMB")
    private String allocatedRamSizeMB;

    @ApiModelProperty(value = "last_Modified")
    private String lastModified;

    @ApiModelProperty(value = "CPU超分比")
    private String cpuRatio;

    @ApiModelProperty(value = "totalRamSizeMB")
    private String totalRamSizeMB;

    @ApiModelProperty(value = "trustLvl")
    private String trustLvl;

    @ApiModelProperty(value = "resourcePoolName")
    private String resourcePoolName;

    @ApiModelProperty(value = "内存超分比")
    private String ramAllocationRatio;

    @ApiModelProperty(value = "磁盘大小（MB）")
    private String totalDiskSizeMB;

    @ApiModelProperty(value = "keystoneId")
    private String keystoneId;

    @ApiModelProperty(value = "logicalRegionId")
    private String logicalRegionId;

    @ApiModelProperty(value = "totalMemory")
    private String totalMemory;

    @ApiModelProperty(value = "regionId")
    private String regionId;

    @ApiModelProperty(value = "service")
    private String service;

    @ApiModelProperty(value = "nativeId")
    private String nativeId;

    @ApiModelProperty(value = "freeDiskSizeMB")
    private String freeDiskSizeMB;
}

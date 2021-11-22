package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

/**
 * 虚拟化服务器容量统计
 */
@Data
public class VirtualMachineCapacityVO {
    private String name;
    private double cpu;
    private double memory;
}

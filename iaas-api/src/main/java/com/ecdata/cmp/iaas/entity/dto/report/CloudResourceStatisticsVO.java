package com.ecdata.cmp.iaas.entity.dto.report;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 云平台资源统计报表
 *
 * @author xuxiaojian
 * @date 2020/5/11 10:50
 */
@Data
public class CloudResourceStatisticsVO {
    @ApiModelProperty("组织名称")
    @Excel(name = "组织名称", height = 10, width = 30, isImportField = "true_st")
    private String name;

    @ApiModelProperty("虚拟机数量")
    @Excel(name = "虚拟机数量", height = 10, width = 30, isImportField = "true_st")
    private int vmNum;

    @ApiModelProperty("已分配cpu统计")
    @Excel(name = "已分配cpu统计", height = 10, width = 30, isImportField = "true_st")
    private int cpuAssigned;

    @ApiModelProperty("申请中cpu统计中")
    @Excel(name = "申请中cpu统计中", height = 10, width = 30, isImportField = "true_st")
    private int cpu;

    @ApiModelProperty("已分配memory统计中")
    @Excel(name = "已分配memory统计中", height = 10, width = 30, isImportField = "true_st")
    private int memoryAssigned;

    @ApiModelProperty("申请中memory统计中")
    @Excel(name = "申请中memory统计中", height = 10, width = 30, isImportField = "true_st")
    private int memory;

    @ApiModelProperty("已分配storage统计中")
    @Excel(name = "已分配storage统计中", height = 10, width = 30, isImportField = "true_st")
    private int storageAssigned;

    @ApiModelProperty("申请中storage统计中")
    @Excel(name = "申请中storage统计中", height = 10, width = 30, isImportField = "true_st")
    private int storage;

    @ApiModelProperty("裸金属")
    @ExcelCollection(name = "裸金属", orderNum = "2")
    private List<BareMetalVO> bareMetalVOS;
}

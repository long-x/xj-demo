package com.ecdata.cmp.iaas.entity.dto.distribution;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/5 18:39
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "云平台资源分配导出对象", description = "云平台资源分配导出对象")
@ExcelTarget("DeptCounExcelVO")
public class DeptCounExcelVO {

    @ApiModelProperty("二级集团")
    @Excel(name = "二级集团", height = 10, width = 30, isImportField = "true_st")
    private String secondDept;
    @ApiModelProperty("三级部门")
    @Excel(name = "三级部门", height = 10, width = 30, isImportField = "true_st")
    private String thirdlyDept;
    @ApiModelProperty("系统数量")
    @Excel(name = "系统数量", height = 10, width = 30, isImportField = "true_st")
    private String systemCon;

    @ApiModelProperty("可用分区")
    @Excel(name = "可用分区", height = 10, width = 30, isImportField = "true_st")
    private String azone;

    @ApiModelProperty("vcpu")
    @Excel(name = "vcpu", height = 10, width = 30, isImportField = "true_st")
    private String vcpu;
    @ApiModelProperty("内存")
    @Excel(name = "内存", height = 10, width = 30, isImportField = "true_st")
    private String memory;

    @ApiModelProperty("磁盘")
    @Excel(name = "磁盘", height = 10, width = 30, isImportField = "true_st")
    private String disk;

    @ApiModelProperty("虚拟机数量")
    @Excel(name = "虚拟机数量", height = 10, width = 30, isImportField = "true_st")
    private String vmcon;

    @ApiModelProperty("裸金属型号1")
    @Excel(name = "裸金属型号1", height = 10, width = 30, isImportField = "true_st")
    private String barecon1;

    @ApiModelProperty("裸金属型号2")
    @Excel(name = "裸金属型号2", height = 10, width = 30, isImportField = "true_st")
    private String barecon2;


}

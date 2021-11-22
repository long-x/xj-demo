package com.ecdata.cmp.iaas.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/15 14:13
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "计量计费统计导出对象", description = "计量计费统计导出对象")
@ExcelTarget("IaasAccountingStatisticsExcelVO")
public class IaasAccountingStatisticsExcelVO extends Model<IaasAccountingStatisticsExcelVO> {




    @ApiModelProperty("组织名称")
    @Excel(name = "组织名称", height = 20, width = 30, isImportField = "true_st")
    private String orgName;

    @ApiModelProperty("生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "生效时间", format = "yyyy-MM-dd", height = 20, width = 30, isImportField = "true_st")
    private Date effectiveDate;

    @ApiModelProperty("cpu数量")
    @Excel(name = "cpu数量", height = 20, width = 30, isImportField = "true_st")
    private Long cpuCount;

    @ApiModelProperty("cpu单价")
    @Excel(name = "cpu单价", height = 20, width = 30, isImportField = "true_st")
    private Long cpuUnitPrice;

    @ApiModelProperty("内存数量")
    @Excel(name = "内存数量", height = 20, width = 30, isImportField = "true_st")
    private Long memoryCount;

    @ApiModelProperty("内存单价")
    @Excel(name = "内存单价", height = 20, width = 30, isImportField = "true_st")
    private Long memoryUnitPrice;

    @ApiModelProperty("磁盘数量")
    @Excel(name = "磁盘数量", height = 20, width = 30, isImportField = "true_st")
    private Long diskCount;

    @ApiModelProperty("磁盘单价")
    @Excel(name = "磁盘单价", height = 20, width = 30, isImportField = "true_st")
    private Long diskUnitPrice;

    @ApiModelProperty("裸金属型号1数量")
    @Excel(name = "裸金属型号1数量", height = 20, width = 30, isImportField = "true_st")
    private Long bmsType1Count;

    @ApiModelProperty("裸金属型号1单价")
    @Excel(name = "裸金属型号1单价", height = 20, width = 30, isImportField = "true_st")
    private Long bmsType1UnitPrice;

    @ApiModelProperty("裸金属型号2数量")
    @Excel(name = "裸金属型号2数量", height = 20, width = 30, isImportField = "true_st")
    private Long bmsType2Count;

    @ApiModelProperty("裸金属型号2单价")
    @Excel(name = "裸金属型号2单价", height = 20, width = 30, isImportField = "true_st")
    private Long bmsType2UnitPrice;

    @ApiModelProperty("合计")
    @Excel(name = "合计", height = 20, width = 30, isImportField = "true_st")
    private Long money;



}

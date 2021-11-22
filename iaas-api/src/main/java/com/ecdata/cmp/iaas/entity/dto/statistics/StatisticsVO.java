package com.ecdata.cmp.iaas.entity.dto.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/15 14:48
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "资源统计对象", description = "资源统计对象")
@ExcelTarget("StatisticsVO")
public class StatisticsVO extends Model<StatisticsVO> {

    /**
     * vcdID
     */
    @ApiModelProperty(value = "vcdID")
    private Long id;

    /**
     * vdc 名称
     */
    @Excel(name = "组织", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "名称")
    private String remark;

    /**
     * 虚拟机数量
     */
    @Excel(name = "虚拟机数量", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "虚拟机数量")
    private Long vmcon;


    /**
     * 已分配cpu
     */
    @Excel(name = "已分配cpu", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disCpu")
    private Long disCpu;

    /**
     * 在途cpu
     */
    @Excel(name = "在途cpu", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disCpu")
    private Long inCpu;

    /**
     * 已分配内存
     */
    @Excel(name = "已分配内存", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disMemory")
    private Long disMemory;

    /**
     * 在途内存
     */
    @Excel(name = "在途内存", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disMemory")
    private Long inMemory;

    /**
     * 已分配存储
     */
    @Excel(name = "已分配存储", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long disDisk;

    /**
     * 在途存储
     */
    @Excel(name = "在途存储", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long inDisk;

    /**
     * 在途裸金属1
     */
    @Excel(name = "在途裸金属1", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long inBM1;

    /**
     * 已分配裸金属1
     */
    @Excel(name = "已分配裸金属1", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long disBM1;

    /**
     * 在途裸金属2
     */
    @Excel(name = "在途裸金属2", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long inBM2;

    /**
     * 已分配裸金属2
     */
    @Excel(name = "已分配裸金属2", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "disDisk")
    private Long disBM2;

}

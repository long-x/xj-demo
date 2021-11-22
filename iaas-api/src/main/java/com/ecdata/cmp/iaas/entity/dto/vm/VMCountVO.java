package com.ecdata.cmp.iaas.entity.dto.vm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/21 16:05
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "云上系统统计对象2", description = "云上系统统计对象2")
@ExcelTarget("VMCountVO")
public class VMCountVO  implements Serializable {


    @ApiModelProperty(value = "业务组id")
    private String id;

    @Excel(name = "区域", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "区域")
    private String remark;

    @Excel(name = "虚拟服务数量", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "虚拟服务数量")
    private String vmCount;

    @Excel(name = "上云时间", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "上云时间")
    private String createTime;


    @Excel(name = "下云时间", height = 20, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "下云时间")
    private String updateTime;

    @Excel(name = "计数", height = 20, width = 30, isImportField = "false_st")
    @ApiModelProperty(value = "计数")
    private String sumDeleted;

}

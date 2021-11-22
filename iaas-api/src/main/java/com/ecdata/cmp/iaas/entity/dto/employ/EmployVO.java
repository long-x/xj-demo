package com.ecdata.cmp.iaas.entity.dto.employ;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/16 20:09
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "计算资源信息", description = "计算资源信息")
public class EmployVO implements Comparable<EmployVO>{


    /**
     * 资源名称
     */
    @Excel(name = "资源名称", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "资源名称")
    private String resourceName;


    /**
     * 区域名称
     */
    @Excel(name = "分区", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "区域名称")
    private String remark;

    /**
     * 已分配
     */
    @Excel(name = "分配数", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "已分配")
    private Long distribution;

    /**
     * 在途数
     */
    @Excel(name = "在途数", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "在途数")
    private Long inTransit;


    /**
     * 剩余数
     */
    @Excel(name = "剩余数", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "剩余数")
    private Long remain;

    /**
     * 合计
     */
    @Excel(name = "合计", height = 10, width = 30, isImportField = "true_st")
    @ApiModelProperty(value = "合计")
    private Long total;


    @Override
    public int compareTo(EmployVO o) {
        return o.getResourceName().compareTo(this.getResourceName());
    }
}

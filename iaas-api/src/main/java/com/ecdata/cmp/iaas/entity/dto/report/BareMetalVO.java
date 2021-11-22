package com.ecdata.cmp.iaas.entity.dto.report;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * @author xuxiaojian
 * @date 2020/5/11 15:07
 */
@Data
@Alias("ReportBareMetalVO")
public class BareMetalVO {

    @ApiModelProperty("已分配裸金属型号")
    private String bareMetalName;

    @ApiModelProperty("已分配裸金属型号")
    @Excel(name = "已分配裸金属型号", height = 10, width = 30, isImportField = "true_st")
    private long bareMetalAssigned;

    @ApiModelProperty("申请中裸金属型号统计中")
    @Excel(name = "申请中裸金属型号统计中", height = 10, width = 30, isImportField = "true_st")
    private long bareMetal;

}

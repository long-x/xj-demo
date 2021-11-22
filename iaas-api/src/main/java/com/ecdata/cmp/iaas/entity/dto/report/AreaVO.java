package com.ecdata.cmp.iaas.entity.dto.report;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/5/14 9:48
 */
@Data
public class AreaVO {

    private Long id;
    @Excel(name = "分区", height = 10, width = 30, isImportField = "true_st")
    private String areaName;
    @Excel(name = "分配数", height = 10, width = 30, isImportField = "true_st")
    private int assigned;
    @Excel(name = "在途数", height = 10, width = 30, isImportField = "true_st")
    private int assigning;
    @Excel(name = "剩余数", height = 10, width = 30, isImportField = "true_st")
    private int remainingNumber;
    @Excel(name = "合计", height = 10, width = 30, isImportField = "true_st")
    private int sum;

    public int sum() {
        return assigned + assigning + remainingNumber;
    }
}

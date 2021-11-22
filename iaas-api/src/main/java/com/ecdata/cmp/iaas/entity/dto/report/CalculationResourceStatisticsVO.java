package com.ecdata.cmp.iaas.entity.dto.report;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.util.List;

/**
 * 计算资源统计表
 *
 * @author xuxiaojian
 * @date 2020/5/14 9:46
 */
@Data
public class CalculationResourceStatisticsVO {
    private Long id;
    @Excel(name = "资源名称", height = 10, width = 30, isImportField = "true_st")
    private String resourceName;
    @ExcelCollection(name = "详细", orderNum = "2")
    private List<AreaVO> children;
}

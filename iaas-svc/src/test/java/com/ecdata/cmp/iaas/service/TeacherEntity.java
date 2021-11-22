package com.ecdata.cmp.iaas.service;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/5/25 9:56
 */
@ExcelTarget("teacherEntity")
@Data
public class TeacherEntity implements java.io.Serializable {
    private String id;
    /**
     * name
     */
    @Excel(name = "主讲老师_major,代课老师_absent", orderNum = "1", needMerge = true, isImportField = "true_major,true_absent")
    private String name;
}

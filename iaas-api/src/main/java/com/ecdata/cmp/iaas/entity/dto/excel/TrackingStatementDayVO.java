package com.ecdata.cmp.iaas.entity.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 1010/6/3 14:34
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "资源跟踪导出对象", description = "资源跟踪导出对象")
@ExcelTarget("TrackingStatementDayVO")
public class TrackingStatementDayVO extends Model<TrackingStatementDayVO> {

    @ApiModelProperty("组织名称")
    @Excel(name = "组织名称", height = 10, width = 30, isImportField = "true_st")
    private String businessName;

    @ApiModelProperty("类型")
    @Excel(name = "类型", height = 10, width = 30, isImportField = "true_st")
    private String resourceName;


    @ApiModelProperty("_01")
    @Excel(name = "一号", height = 10, width = 15, isImportField = "true_st")
    private String _01;


    @ApiModelProperty("_02")
    @Excel(name = "二号", height = 10, width = 15, isImportField = "true_st")
    private String _02;

    @ApiModelProperty("_03")
    @Excel(name = "三号", height = 10, width = 15, isImportField = "true_st")
    private String _03;

    @ApiModelProperty("_04")
    @Excel(name = "四号", height = 10, width = 15, isImportField = "true_st")
    private String _04;

    @ApiModelProperty("_05")
    @Excel(name = "五号", height = 10, width = 15, isImportField = "true_st")
    private String _05;

    @ApiModelProperty("_06")
    @Excel(name = "六号", height = 10, width = 15, isImportField = "true_st")
    private String _06;

    @ApiModelProperty("_07")
    @Excel(name = "七号", height = 10, width = 15, isImportField = "true_st")
    private String _07;

    @ApiModelProperty("_08")
    @Excel(name = "八号", height = 10, width = 15, isImportField = "true_st")
    private String _08;

    @ApiModelProperty("_09")
    @Excel(name = "九号", height = 10, width = 15, isImportField = "true_st")
    private String _09;

    @ApiModelProperty("_10")
    @Excel(name = "十号", height = 10, width = 15, isImportField = "true_st")
    private String _10;

    @ApiModelProperty("_11")
    @Excel(name = "十一号", height = 10, width = 15, isImportField = "true_st")
    private String _11;

    @ApiModelProperty("_12")
    @Excel(name = "十二号", height = 10, width = 15, isImportField = "true_st")
    private String _12;

    @ApiModelProperty("_13")
    @Excel(name = "十三号", height = 10, width = 15, isImportField = "true_st")
    private String _13;

    @ApiModelProperty("_14")
    @Excel(name = "十四号", height = 10, width = 15, isImportField = "true_st")
    private String _14;

    @ApiModelProperty("_15")
    @Excel(name = "十五号", height = 10, width = 15, isImportField = "true_st")
    private String _15;

    @ApiModelProperty("_16")
    @Excel(name = "十六号", height = 10, width = 15, isImportField = "true_st")
    private String _16;

    @ApiModelProperty("_17")
    @Excel(name = "十七号", height = 10, width = 15, isImportField = "true_st")
    private String _17;

    @ApiModelProperty("_18")
    @Excel(name = "十八号", height = 10, width = 15, isImportField = "true_st")
    private String _18;

    @ApiModelProperty("_19")
    @Excel(name = "十九号", height = 10, width = 15, isImportField = "true_st")
    private String _19;

    @ApiModelProperty("_20")
    @Excel(name = "二十号", height = 10, width = 15, isImportField = "true_st")
    private String _20;

    @ApiModelProperty("_21")
    @Excel(name = "二十一号", height = 10, width = 15, isImportField = "true_st")
    private String _21;

    @ApiModelProperty("_22")
    @Excel(name = "二十二号", height = 10, width = 15, isImportField = "true_st")
    private String _22;

    @ApiModelProperty("_23")
    @Excel(name = "二十三号", height = 10, width = 15, isImportField = "true_st")
    private String _23;

    @ApiModelProperty("_24")
    @Excel(name = "二十四号", height = 10, width = 15, isImportField = "true_st")
    private String _24;

    @ApiModelProperty("_25")
    @Excel(name = "二十五号", height = 10, width = 15, isImportField = "true_st")
    private String _25;

    @ApiModelProperty("_26")
    @Excel(name = "二十六号", height = 10, width = 15, isImportField = "true_st")
    private String _26;

    @ApiModelProperty("_27")
    @Excel(name = "二十七号", height = 10, width = 15, isImportField = "true_st")
    private String _27;

    @ApiModelProperty("_28")
    @Excel(name = "二十八号", height = 10, width = 15, isImportField = "true_st")
    private String _28;

    @ApiModelProperty("_29号")
    @Excel(name = "二十九", height = 10, width = 15, isImportField = "true_st")
    private String _29;

    @ApiModelProperty("_30号")
    @Excel(name = "三十", height = 10, width = 15, isImportField = "true_st")
    private String _30;

    @ApiModelProperty("_31号")
    @Excel(name = "三十一", height = 10, width = 15, isImportField = "true_st")
    private String _31;


}

package com.ecdata.cmp.huawei.dto.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "监控简单对象", description = "监控简单对象")
public class SimpleIndicator implements Serializable {
    private static final long serialVersionUID = 7506378920903443935L;

    /**
     * kpi
     */
    @ApiModelProperty(value = "kpi")
    private String kpi;

    /**
     * 数据类型
     */
    @ApiModelProperty(value = "数据类型")
    private String data_type;


    /**
     * 数据单位
     */
    @ApiModelProperty(value = "数据单位")
    private String data_unit;

    /**
     * 英文描述
     */
    @ApiModelProperty(value = "英文描述")
    private String en_us;

    /**
     * 中文描述
     */
    @ApiModelProperty(value = "中文描述")
    private String zh_cn;

    /**
     * 组英文描述
     */
    @ApiModelProperty(value = "组英文描述")
    private String group_en_us;

    /**
     * 组中文描述
     */
    @ApiModelProperty(value = "组中文描述")
    private String group_zh_cn;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String tag;

    /**
     * alarm_id
     */
    @ApiModelProperty(value = "alarm_id")
    private String alarm_id;


    /**
     * alarm_desc_en_us
     */
    @ApiModelProperty(value = "alarm_desc_en_us")
    private String alarm_desc_en_us;

    /**
     * alarm_desc_zh_cn
     */
    @ApiModelProperty(value = "alarm_desc_zh_cn")
    private String alarm_desc_zh_cn;

    /**
     * indicator_name
     */
    @ApiModelProperty(value = "indicator_name")
    private String indicator_name;
}

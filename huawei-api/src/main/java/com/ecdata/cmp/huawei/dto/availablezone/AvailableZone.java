package com.ecdata.cmp.huawei.dto.availablezone;

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
@ApiModel(value = "可用区", description = "可用区")
public class AvailableZone implements Serializable {
    private static final long serialVersionUID = -3243210280658065920L;
    /**
     * 可用分区ID
     */
    @ApiModelProperty(value = "可用分区ID")
    private String az_id;

    /**
     * region_id
     */
    @ApiModelProperty(value = "region_id")
    private String region_id;

    /**
     * 云资源池id
     */
    @ApiModelProperty(value = "云资源池id")
    private String cloud_infra_id;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 可用分区名称
     */
    @ApiModelProperty(value = "可用分区名称")
    private String name;

    /**
     * 可用分区类型
     */
    @ApiModelProperty(value = "可用分区类型")
    private String type;

    /**
     * 可用分区状态：normal，正常，abnormal异常
     */
    @ApiModelProperty(value = "可用分区状态：normal，正常，abnormal异常")
    private String status;

    /**
     * 可用分区属性
     */
    @ApiModelProperty(value = "可用分区属性")
    private String extend_param;
}

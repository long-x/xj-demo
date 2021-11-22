package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "CI实例数据", description = "CI实例数据")
public class CiInstanceVO implements Serializable {
    private static final long serialVersionUID = -1276631916596031179L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "可用分区名称")
    private String azoneName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "集群Id")
    private String clusterId;

    @ApiModelProperty(value = "集群名称")
    private String clusterName;

    @ApiModelProperty(value = "vdcId")
    private String vdcId;

    @ApiModelProperty(value = "bizRegionId")
    private String bizRegionId;

    @ApiModelProperty(value = "imageId")
    private String imageId;

    @ApiModelProperty(value = "floatingIp")
    private String floatingIp;

    @ApiModelProperty(value = "hostId")
    private String hostId;

    @ApiModelProperty(value = "resId")
    private String resId;

    @ApiModelProperty(value = "resourcePoolName")
    private String resourcePoolName;

    @ApiModelProperty(value = "class_Name")
    private String class_Name;

    @ApiModelProperty(value = "azoneId")
    private String azoneId;

    @ApiModelProperty(value = "regionId")
    private String regionId;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "vdcName")
    private String vdcName;

    @ApiModelProperty(value = "nativeId")
    private String nativeId;

    @ApiModelProperty(value = "projectId")
    private String projectId;

    @ApiModelProperty(value = "vmState")
    private String vmState;

    @ApiModelProperty(value = "bizRegionName")
    private String bizRegionName;

    @ApiModelProperty(value = "cpuCoreNum")
    private String cpuCoreNum;

    @ApiModelProperty(value = "status")
    private String status;
}

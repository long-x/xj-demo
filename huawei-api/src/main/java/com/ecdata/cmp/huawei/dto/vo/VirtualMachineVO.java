package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机对象", description = "虚拟机对象")
public class VirtualMachineVO {


    @ApiModelProperty(value = "name")
    private Map<String, Object> name;

    @ApiModelProperty(value = "status")
    private String status;

    @ApiModelProperty(value = "privateIps")
    private String privateIps;

    @ApiModelProperty(value = "projectId")
    private String projectId;

    @ApiModelProperty(value = "cpu使用率")
    private float cpuUsage;

    @ApiModelProperty(value = "硬盘使用率")
    private String diskUsage;

    @ApiModelProperty(value = "bizRegionName")
    private String bizRegionName;

    @ApiModelProperty(value = "bizRegionId")
    private String bizRegionId;

    @ApiModelProperty(value = "内存使用率")
    private float memoryUsage;

    @ApiModelProperty(value = "floatingIp")
    private String floatingIp;

    @ApiModelProperty("虚拟机镜像名称")
    private String imageName;

    @ApiModelProperty(value = "supportHistoryPerf")
    private boolean supportHistoryPerf;

    @ApiModelProperty(value = "flavor")
    private String flavor;

    @ApiModelProperty(value = "vdc名称")
    private String vdcName;

    @ApiModelProperty(value = "os版本")
    private String osVersion;

    @ApiModelProperty(value = "虚拟机ID")
    private String nativeId;

    @ApiModelProperty(value = "虚拟机ID")
    private VirtualMachineConditionVO condition;

    @ApiModelProperty(value = "区域ID")
    private String azoneId;

    @ApiModelProperty(value = "区域名称")
    private String azoneName;

    @ApiModelProperty(value = "主机id")
    private String physicalHostId;

    public String resId() {
        if (condition == null) {
            return "";
        }
        return condition.getResId();
    }
}

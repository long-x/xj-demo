package com.ecdata.cmp.huawei.dto.availablezone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SysAzone对象", description = "SysAzone对象")
public class SysAzone {
    @ApiModelProperty("ownerType")
    private String ownertype;

    @ApiModelProperty("globalName")
    private String globalname;

    @ApiModelProperty("manager")
    private boolean manager;

    @ApiModelProperty("regionName")
    private String regionname;

    @ApiModelProperty("ownerId")
    private String ownerid;

    @ApiModelProperty("resId")
    private String resid;

    @ApiModelProperty("deviceName")
    private String devicename;

    @ApiModelProperty("resourcePoolName")
    private String resourcepoolname;

    @ApiModelProperty("keystoneId")
    private String keystoneid;

    @ApiModelProperty("logicalRegionId")
    private String logicalregionid;

    @ApiModelProperty("class_Name")
    private String className;

    @ApiModelProperty("ownerName")
    private String ownername;

    @ApiModelProperty("regionId")
    private String regionid;

    @ApiModelProperty("resourcePoolId")
    private String resourcepoolid;

    @ApiModelProperty("cloudType")
    private String cloudtype;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("confirmStatus")
    private String confirmstatus;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("last_Modified")
    private Long lastModified;

    @ApiModelProperty("nativeId")
    private String nativeid;

    @ApiModelProperty("bizRegionId")
    private String bizregionid;

    @ApiModelProperty("virtualizeType")
    private String virtualizeType;
}

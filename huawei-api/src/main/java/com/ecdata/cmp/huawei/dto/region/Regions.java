package com.ecdata.cmp.huawei.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "区域对象", description = "区域对象")
public class Regions implements Serializable {
    private static final long serialVersionUID = 9150722010487629867L;

    /**
     * 位置
     */
    @ApiModelProperty(value = "位置")
    private String locales;

    /**
     * cloud_infras
     */
    @ApiModelProperty(value = "cloud_infras")
    private String cloud_infras;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * parent_region_id
     */
    @ApiModelProperty(value = "parent_region_id")
    private String parent_region_id;

}

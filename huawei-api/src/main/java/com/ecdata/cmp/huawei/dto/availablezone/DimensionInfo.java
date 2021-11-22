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
@ApiModel(value = "维度", description = "维度")
public class DimensionInfo implements Serializable {
    private static final long serialVersionUID = 450033768720326271L;

    /**
     * 维度类型
     */
    @ApiModelProperty(value = "维度类型")
    private String dimensionType;

    /**
     * 资源值
     */
    @ApiModelProperty(value = "资源值")
    private String dimensionValue;

    /**
     * 资源值
     */
    @ApiModelProperty(value = "资源值")
    private String volumeTypeOriginal;
}

package com.ecdata.cmp.huawei.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "区域信息对象", description = "区域信息对象")
public class RegionInfo implements Serializable {
    private static final long serialVersionUID = -7441577086636717732L;
    /**
     * 区域列表
     */
    @ApiModelProperty(value = "区域列表")
    private Map<String,Regions> regions;

    /**
     * 容量按照统一单位计量时的容量数值
     */
    @ApiModelProperty(value = "容量按照统一单位计量时的容量数值")
    private String capacityValue;
}

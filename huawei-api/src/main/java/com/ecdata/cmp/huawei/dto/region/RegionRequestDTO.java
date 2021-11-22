package com.ecdata.cmp.huawei.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "区域传参封装对象", description = "区域传参封装对象")
public class RegionRequestDTO {
    @ApiModelProperty(value = "运营面Token")
    private String ocToken;

    @ApiModelProperty(value = "运维面Token")
    private String omToken;

    @ApiModelProperty(value = "区域ID")
    private String regionId;
}

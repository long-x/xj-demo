package com.ecdata.cmp.iaas.entity.dto;

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
@ApiModel(value = "可用分区传参封装对象", description = "可用分区传参封装对象")
public class AvailbleZoneReqVO {
    @ApiModelProperty(value = "运营面Token")
    private String ocToken;

    @ApiModelProperty(value = "运维面Token")
    private String omToken;

    @ApiModelProperty(value = "可用区ID")
    private String azoneId;
}

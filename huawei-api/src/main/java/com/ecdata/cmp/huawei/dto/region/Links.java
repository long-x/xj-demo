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
@ApiModel(value = "区域链接", description = "区域链接")
public class Links implements Serializable {
    private static final long serialVersionUID = -5847469830103660789L;
    /**
     * next
     */
    @ApiModelProperty(value = "next")
    private String next;

    /**
     * self
     */
    @ApiModelProperty(value = "self")
    private String self;

    /**
     * previous
     */
    @ApiModelProperty(value = "previous")
    private String previous;

}

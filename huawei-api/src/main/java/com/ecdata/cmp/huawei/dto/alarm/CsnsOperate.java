package com.ecdata.cmp.huawei.dto.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CsnsOperate操作对象", description = "CsnsOperate操作对象")
public class CsnsOperate implements Serializable {
    private static final long serialVersionUID = -5823774873931752843L;
    /**
     * 告警id
     */
    @ApiModelProperty(value = "告警id")
    private Long[] csns;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;
}

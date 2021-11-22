package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/11/20 16:03
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "请求华为认证返回对象", description = "请求华为认证返回对象")
public class TokenInfoVO implements Serializable {
    private static final long serialVersionUID = -926623071764063955L;

    /**
     * 运营面Token
     */
    @ApiModelProperty(value = "运营面Token(oc)")
    private String ocToken;

    /**
     * 运维面Token
     */
    @ApiModelProperty(value = "运维面Token")
    private OMToken omToken;

    /** 运维面Web Token */
    @ApiModelProperty(value = "运维面Web Token")
    private OMToken omTokenWeb;

}

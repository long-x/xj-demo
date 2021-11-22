package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/11/20 14:47
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@ApiModel(value = "请求华为认证传输对象", description = "请求华为认证传输对象")
public class TokenInfoDTO implements Serializable {
    private static final long serialVersionUID = -1490437070615186742L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String value;

    /**
     * 授权类型
     */
    @ApiModelProperty(value = "授权类型")
    private String grantType;

}

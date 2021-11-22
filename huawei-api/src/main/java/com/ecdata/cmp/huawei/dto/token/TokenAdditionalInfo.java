package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description 请求华为认证返回对象额外信息
 * @date 2019/11/20 16:05
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "请求华为认证返回对象额外信息", description = "请求华为认证返回对象额外信息")
public class TokenAdditionalInfo implements Serializable {
    private static final long serialVersionUID = 5544555303888925572L;

    /** 密码有效期剩余时间 */
    @ApiModelProperty(value = "密码有效期剩余时间")
    private Long expires;

    /** 密码即将过期状态*/
    @ApiModelProperty(value = "密码即将过期状态")
    private String passwdStatus;
}

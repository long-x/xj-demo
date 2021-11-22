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
 * @date 2019/12/2 14:49
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "请求Token的Auth对象", description = "请求Token的Auth对象")
public class Auth implements Serializable {

    private static final long serialVersionUID = -4766349113908421769L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "Token对象中的身份")
    private Identity identity;

    /**
     * 密码
     */
    @ApiModelProperty(value = "Token对象中的范围")
    private Scope scope;
}

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
 * @date 2019/12/2 15:00
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token对象中的user", description = "Token对象中的user")
public class User implements Serializable {

    private static final long serialVersionUID = -5989538032012240328L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 密码
     */
    @ApiModelProperty(value = "领域")
    private Domain domain;
}

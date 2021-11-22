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
 * @date 2019/12/2 16:01
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token identity对象中的Password", description = "Token identity对象中的Password")
public class Password implements Serializable {

    private static final long serialVersionUID = -7144443119247462842L;
    /**
     *user
     */
    @ApiModelProperty(value = "user")
    private User user;
}

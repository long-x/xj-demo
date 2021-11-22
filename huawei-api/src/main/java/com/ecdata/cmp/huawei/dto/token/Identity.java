package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author ty
 * @description
 * @date 2019/12/2 14:54
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token Auth对象中的identity", description = "Token Auth对象中的identity")
public class Identity implements Serializable {
    private static final long serialVersionUID = 5234553894646684010L;

    /**
     * token中的methods
     */
    @ApiModelProperty(value = "identity中的methods")
    private List<String> methods;

    /**
     * 密码对象
     */
    @ApiModelProperty(value = "密码对象")
    private Password password;
}

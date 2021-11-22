package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "请求华为Token对象", description = "请求华为Token对象")
public class TokenDTO implements Serializable {
    /**
     * 运维token
     */
    @ApiModelProperty(value = "运维token")
    private String omToken;

    /**
     * 运营token
     */
    @ApiModelProperty(value = "运营token")
    private String ocToken;
}

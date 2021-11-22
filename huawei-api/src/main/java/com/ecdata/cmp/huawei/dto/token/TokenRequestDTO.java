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
 * @date 2019/12/2 15:47
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "请求华为获取Token对象", description = "请求华为获取Token对象")
public class TokenRequestDTO implements Serializable {
    private static final long serialVersionUID = 8184868421790998263L;

    /**
     *auth
     */
    @ApiModelProperty(value = "auth")
    private Auth auth;
}

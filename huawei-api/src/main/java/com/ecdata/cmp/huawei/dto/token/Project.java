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
 * @date 2019/12/2 15:12
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token Scope对象中的Project", description = "Token对象中的user")
public class Project implements Serializable {

    private static final long serialVersionUID = 785393056180752047L;

    /**
     *project id
     */
    @ApiModelProperty(value = "id")
    private String id;
}

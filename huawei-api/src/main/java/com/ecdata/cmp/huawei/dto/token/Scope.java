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
 * @date 2019/12/2 15:11
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token对象中的Scope", description = "Token对象中的Scope")
public class Scope implements Serializable {
    private static final long serialVersionUID = 7070416858247124508L;

    /**
     * domain
     */
    @ApiModelProperty(value = "domain")
    private Domain domain;

    /**
     * 项目
     */
    @ApiModelProperty(value = "项目")
    private Project project;
}

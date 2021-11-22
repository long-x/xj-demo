package com.ecdata.cmp.user.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: BimSchemaAccountResp
 * @Author: shig
 * @description: 获取账号等对象全部属性信息
 * @Date: 2020/3/5 6:36 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BimSchemaChildResp implements Serializable {

    private static final long serialVersionUID = 6623355004312105487L;
    @ApiModelProperty(value = "对象的属性字段是否为多值")
    private Boolean multivalued;

    @ApiModelProperty(value = "对象的属性字段名称")
    private String name;

    @ApiModelProperty(value = "对象的属性字段在创建时是否为必填字段")
    private Boolean required;

    @ApiModelProperty(value = "对象的属性字段类型")
    private String type;

    @ApiModelProperty(value = "对象的属性字段描述")
    private String comment;
}
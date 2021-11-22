package com.ecdata.cmp.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @title: BimSchema
 * @Author: shig
 * @description: 获取账号等对象全部属性信息
 * @Date: 2020/3/5 6:30 下午
 */
@Data
@ApiModel(value = "获取账号等对象全部属性信息", description = "获取账号等对象全部属性信息")
public class BimSchemaResp implements Serializable {
    private static final long serialVersionUID = -635339772787174212L;
    @ApiModelProperty(value = "请求 ID")
    private String bimRequestId;

    @ApiModelProperty(value = "账号")
    private BimSchemaChildResp[] account;

    @ApiModelProperty(value = "组织机构")
    private BimSchemaChildResp[] organization;

}
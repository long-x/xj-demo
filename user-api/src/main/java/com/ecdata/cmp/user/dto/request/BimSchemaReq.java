package com.ecdata.cmp.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @title: BimSchema
 * @Author: shig
 * @description: 竹云 “账号”等对象全部属性信息
 * @Date: 2020/3/5 6:30 下午
 */
@Data
@ApiModel(value = "竹云账号请求对象", description = "竹云账号请求对象")
public class BimSchemaReq implements Serializable {

    private static final long serialVersionUID = 7383092961293300351L;
    @ApiModelProperty(value = "请求 ID")
    private String bimRequestId;

    @ApiModelProperty(value = "用户")
    private String bimRemoteUser;

    @ApiModelProperty(value = "密码")
    private String bimRemotePwd;

}
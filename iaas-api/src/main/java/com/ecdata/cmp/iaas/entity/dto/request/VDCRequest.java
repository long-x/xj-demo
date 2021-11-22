package com.ecdata.cmp.iaas.entity.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 描述:vdc同步数据请求参数
 *
 * @author xxj
 * @create 2019-11-14 14:24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "VDC请求对象", description = "VDC请求对象")
public class VDCRequest implements Serializable {
    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "供应商用户名")
    private String providerUsername;

    @ApiModelProperty(value = "供应商密码")
    private String providerPassword;

    @ApiModelProperty(value = "供应商ID")
    private Long providerId;

    @ApiModelProperty(value = "vdcId")
    private Long vdcId;

    @ApiModelProperty(value = "vdcKey")
    private String vdcKey;

    @ApiModelProperty(value = "domainName")
    private String domainName;
}

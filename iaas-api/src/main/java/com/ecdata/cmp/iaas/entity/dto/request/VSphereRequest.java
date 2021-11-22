package com.ecdata.cmp.iaas.entity.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-14 14:24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "VSphere请求对象", description = "VSphere请求对象")
public class VSphereRequest implements Serializable {
    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "供应商ID")
    private Long providerId;

    @ApiModelProperty(value = "同步类型(华为，vsphere)")
    private String syncType;

    /**
     * 运营面地址
     */
    @ApiModelProperty(value = "运营面地址:华为")
    private String authAddress;

    /**
     * 运维面调用地址
     */
    @ApiModelProperty(value = "运维面调用地址:华为")
    private String ocAddress;

    /**
     * 弹性云服务调用地址
     */
    @ApiModelProperty(value = "弹性云服务调用地址:华为")
    private String ecsAddress;

    /**
     * 云硬盘调用地址
     */
    @ApiModelProperty(value = "云硬盘调用地址:华为")
    private String evsAddress;

    /**
     * 虚拟私有云调用地址
     */
    @ApiModelProperty(value = "虚拟私有云调用地址:华为")
    private String vpcAddress;

    public String address() {
        if (this == null) {
            return "";
        }

        return StringUtils.isBlank(this.address) ? "" : this.address;
    }

    public String username() {
        if (this == null) {
            return "";
        }

        return StringUtils.isBlank(this.username) ? "" : this.username;
    }

    public String password() {
        if (this == null) {
            return "";
        }

        return StringUtils.isBlank(this.password) ? "" : this.password;
    }
}

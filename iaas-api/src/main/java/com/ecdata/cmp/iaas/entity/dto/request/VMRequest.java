package com.ecdata.cmp.iaas.entity.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 描述:vdc同步数据请求参数
 *
 * @author xxj
 * @create 2019-11-14 14:24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "VM请求对象", description = "VM请求对象")
public class VMRequest implements Serializable {
    @ApiModelProperty(value = "供应商ID")
    private Long providerId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目projectKey")
    private String projectKey;

    @ApiModelProperty(value = "vdc域名称")
    private String domainName;

    @ApiModelProperty(value = "vdc用户名")
    private String username;

    @ApiModelProperty(value = "vdc密码")
    private String password;

    @ApiModelProperty(value = "集群key")
    private String clusterKey;

    @ApiModelProperty(value = "业务组id")
    private Long businessId;

    @ApiModelProperty(value = "资源池id")
    private Long poolId;

    @ApiModelProperty(value = "业务组ids")
    private String[] businessIds;

    @ApiModelProperty(value = "vmKeyList")
    private List<String> vmKeyList;
}

package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * API接口 GET /v1/{tenant_id}/subnets
 * @author ：xuj
 * @date ：Created in 2019/12/4 15:11
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "子网列表返回对象", description = "子网列表返回对象")
public class SubnetsVO {

    @ApiModelProperty(value = "子网ID")
    private String id;

    @ApiModelProperty(value = "子网名称")
    private String name;

    @ApiModelProperty(value = "子网CIDR")
    private String cidr;

    @ApiModelProperty(value = "dns列表")
    private String dnsList;

    @ApiModelProperty(value = "status")
    private String status;

    @ApiModelProperty(value = "true标识直连网络")
    private boolean external;

    @ApiModelProperty(value = "指示是否是VPC路由网络（true）、VPC内部网络（false）")
    private boolean routed;

    @ApiModelProperty(value = "子网所在VPC ID")
    private String vpcId;

    @ApiModelProperty(value = "project_id")
    private String projectId;

    @ApiModelProperty(value = "子网网关")
    private String gatewayIp;

    @ApiModelProperty(value = "是否使能DHCP")
    private boolean dhcpEnable;

    @ApiModelProperty(value = "子网DNS服务器地址，IP格式")
    private String primaryDns;

    @ApiModelProperty(value = "子网DNS服务器地址，IP格式")
    private String secondaryDns;

    @ApiModelProperty(value = "host_routes")
    private String hostRoutes;

    @ApiModelProperty(value = "动态分配IP地址的地址池")
    private List<Map<String,Object>> allocation_pools;

    @ApiModelProperty(value = "虚拟网络的VLAN ID")
    private String segmentation_id;

    @ApiModelProperty(value = "neutron_subnet_id")
    private String neutron_subnet_id;

    @ApiModelProperty(value = "ip_version")
    private String ip_version;

    @ApiModelProperty(value = "neutron_network_id")
    private String neutron_network_id;


}


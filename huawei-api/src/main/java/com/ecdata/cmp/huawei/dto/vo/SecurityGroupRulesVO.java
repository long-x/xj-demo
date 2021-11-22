package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 17:04
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "资源详细信息返回对象", description = "资源详细信息返回对象")
public class SecurityGroupRulesVO {

    @ApiModelProperty(value = "安全组的id")
    private String id;

    @ApiModelProperty(value = "priority")
    private String priority;

    @ApiModelProperty(value = "action")
    private String action;

    @ApiModelProperty(value = "规则方向，取值范围ingress/egress")
    private String direction;

    @ApiModelProperty(value = "协议类型或直接指定IP协议号")
    private String protocol;

    @ApiModelProperty(value = "网络类型，取值范围IPv4/IPv6")
    private String ethertype;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "所属安全组的对端id。与remote_ip_prefix参数二选一")
    private String remoteGroupId;

    @ApiModelProperty(value = "对端ip网段。与remote_group_id参数二选一")
    private String remoteIpPrefix;

    @ApiModelProperty(value = "租户id，只有管理员用户才允许指定非本租户的tenant_id")
    private String tenantId;

    /**
     * 最大端口，当协议类型为ICMP时，该值表示ICMP的code。
     * 范围：1-65535（当表示code时是0-255）
     */
    @ApiModelProperty(value = "最大端口")
    private String portRangeMax;

    /**
     * 最小端口，当协议类型为ICMP时，该值表示ICMP的type。
     * protocol为tcp和udp时，
     * port_range_max和port_range_min必须同时输入，
     * 且port_range_max应大于等于port_range_min。
     * protocol为icmp时，指定ICMPcode（port_range_max）时，必须同时指定ICMP type（port_range_min）。
     * 范围：1-65535（当表示code时是0-255）
     */
    @ApiModelProperty(value = "最小端口")
    private String portRangeMin;

    @ApiModelProperty(value = "所属安全组id")
    private String securityGroupId;

    @ApiModelProperty(value = "资源创建时间")
    private String createdAt;

    @ApiModelProperty(value = "资源更新时间")
    private String updatedAt;

    @ApiModelProperty(value = "资源project_id")
    private String projectId;

}

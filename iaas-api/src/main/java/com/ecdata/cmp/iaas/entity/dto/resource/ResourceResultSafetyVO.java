package com.ecdata.cmp.iaas.entity.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/17 23:37
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "安全资源", description = "安全资源")
public class ResourceResultSafetyVO {

    @ApiModelProperty("下一代防火墙")
    private int next;

    @ApiModelProperty("Web应用防火墙")
    private int web;

    @ApiModelProperty("入侵防御系统")
    private int ips;

    @ApiModelProperty("应用交付（AD）")
    private int ad;

    @ApiModelProperty("上网行为管理")
    private int ac;

    @ApiModelProperty("数据库审计（DAS）")
    private int das;

    @ApiModelProperty("SSL VPN")
    private int vpn;

    @ApiModelProperty("EDR")
    private int edr;

    @ApiModelProperty("日志审计")
    private int log;

    @ApiModelProperty("漏洞扫描")
    private int scan;

    @ApiModelProperty("深信服运维安全管理系统软件")
    private int sinfors;

    @ApiModelProperty("深信服配置核查系统软件")
    private int sinforsCheck;

    @ApiModelProperty("是否防篡改")
    private int changes;

}

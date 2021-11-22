package com.ecdata.cmp.iaas.entity.apply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxiaojian
 * @date 2020/3/3 14:57
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_apply_service_security_resources")
@ApiModel(value = "服务安全资源表", description = "服务安全资源表")
public class IaasApplyServiceSecurityResources extends Model<IaasApplyServiceSecurityResources> {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("配置id")
    private Long configId;

    @ApiModelProperty("是否数据库审计（das）：0，不选，1.必选，默认：0")
    private String isDas;

    @ApiModelProperty("数据库审计（das）")
    private String das;

    @ApiModelProperty("是否ssl vpn：0，不选，1.必选，默认：0")
    private String isSslVpn;

    @ApiModelProperty("ssl vpn")
    private String sslVpn;

    @ApiModelProperty("是否edr：0，不选，1.必选，默认：0")
    private String isEdr;

    @ApiModelProperty("edr")
    private String edr;

    @ApiModelProperty("是否日志审计：0，不选，1.必选，默认：0")
    private String isLogAudit;

    @ApiModelProperty("日志审计")
    private String logAudit;

    @ApiModelProperty("是否漏洞扫描：0，不选，1.必选，默认：0")
    private String isLoopholeScan;

    @ApiModelProperty("漏洞扫描")
    private String loopholeScan;

    @ApiModelProperty("是否深信服运维安全管理系统软件：0，不选，1.必选，默认：0")
    private String isSinfors;

    @ApiModelProperty("深信服运维安全管理系统软件")
    private String sinfors;

    @ApiModelProperty("是否深信服配置核查系统软件：0，不选，1.必选，默认：0")
    private String isSinforsCheck;

    @ApiModelProperty("深信服配置核查系统软件")
    private String sinforsCheck;

    @ApiModelProperty("是否防篡改：0，不选，1.必选，默认：0")
    private String isPreventChange;

    @ApiModelProperty("是否下一代防火墙")
    private String isNextNfw;

    @ApiModelProperty("下一代防火墙")
    private String nextNfw;

    @ApiModelProperty("下一代防火墙数量")
    private Integer nextNfwNum;

    @ApiModelProperty("是否web应用防火墙")
    private String isWebNfw;

    @ApiModelProperty("web应用防火墙")
    private String webNfw;

    @ApiModelProperty("web应用防火墙数量")
    private Integer webNfwNum;

    @ApiModelProperty("是否入侵防御系统")
    private String isIps;

    @ApiModelProperty("入侵防御系统")
    private String ips;

    @ApiModelProperty("入侵防御系统数量")
    private Integer ipsNum;

    @ApiModelProperty("是否应用交付（ad）")
    private String isAd;

    @ApiModelProperty("应用交付（ad）")
    private String ad;

    @ApiModelProperty("应用交付（ad）数量")
    private Integer adNum;

    @ApiModelProperty("是否上网行为管理")
    private String isSinforAc;

    @ApiModelProperty("上网行为管理")
    private String sinforAc;

    @ApiModelProperty("上网行为管理数量")
    private Integer sinforAcNum;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;

    @ApiModelProperty("杀毒软件)")
    private String isAntivirus;

    @ApiModelProperty("堡垒机)")
    private String isBastionHost;

    @ApiModelProperty("跳板机)")
    private String  isSpringBoard;


    @ApiModelProperty("安全感知平台)")
    private String isSecurityPlatform;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

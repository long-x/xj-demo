package com.ecdata.cmp.iaas.entity.dto.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/11 11:26
 */
@Data
public class ConfigInfo {
    private Long configId;
    private String serverName;
    private String applyType;
    private String model;
    private String areaName;
    private int cpu;
    private int memory;
    private String state;

    @ApiModelProperty("是否数据库审计（das）：0，不选，1.必选，默认：0")
    private String isDas;

    @ApiModelProperty("是否ssl vpn：0，不选，1.必选，默认：0")
    private String isSslVpn;

    @ApiModelProperty("是否edr：0，不选，1.必选，默认：0")
    private String isEdr;

    @ApiModelProperty("是否日志审计：0，不选，1.必选，默认：0")
    private String isLogAudit;

    @ApiModelProperty("是否漏洞扫描：0，不选，1.必选，默认：0")
    private String isLoopholeScan;

    @ApiModelProperty("是否深信服运维安全管理系统软件：0，不选，1.必选，默认：0")
    private String isSinfors;

    @ApiModelProperty("是否深信服配置核查系统软件：0，不选，1.必选，默认：0")
    private String isSinforsCheck;

    @ApiModelProperty("是否防篡改：0，不选，1.必选，默认：0")
    private String isPreventChange;

    @ApiModelProperty("是否下一代防火墙")
    private String isNextNfw;

    @ApiModelProperty("是否web应用防火墙")
    private String isWebNfw;

    @ApiModelProperty("是否入侵防御系统")
    private String isIps;

    @ApiModelProperty("是否应用交付（ad）")
    private String isAd;

    @ApiModelProperty("是否上网行为管理")
    private String isSinforAc;

    private List<Storage> storageList;

    public int storage() {
        if (this == null || CollectionUtils.isEmpty(this.getStorageList())) {
            return 0;
        }
        return this.storageList.stream().mapToInt(Storage::getMemoryDisk).sum();
    }
}

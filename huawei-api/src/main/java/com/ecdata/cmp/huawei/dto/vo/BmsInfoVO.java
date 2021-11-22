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
 * @date ：Created in 2020/4/16 13:54
 * @modified By：
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "裸金属信息", description = "裸金属信息")
public class BmsInfoVO {

    /**
     * 规格id  physical.2488
     */
    @ApiModelProperty(value = "规格id")
    private String id;

    /**
     * 弹性ip
     */
    @ApiModelProperty(value = "弹性ip")
    private String floatingIp;

    /**
     * 资源池名称
     */
    @ApiModelProperty(value = "资源池名称")
    private String resourcePoolName;

    /**
     * resId
     */
    @ApiModelProperty(value = "resId")
    private String resId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String bizRegionName;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * vdc名称
     */
    @ApiModelProperty(value = "vdc名称")
    private String vdcName;

    /**
     * nativeId
     */
    @ApiModelProperty(value = "nativeId")
    private String nativeId;

    /**
     * supportHistoryPerf
     */
    @ApiModelProperty(value = "supportHistoryPerf")
    private String supportHistoryPerf;

    /**
     * regionId
     */
    @ApiModelProperty(value = "regionId")
    private String regionId;

    /**
     * bizRegionId
     */
    @ApiModelProperty(value = "bizRegionId")
    private String bizRegionId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String value;

    /**
     * 可用分区
     */
    @ApiModelProperty(value = "可用分区")
    private String azoneInfo;

    /**
     * supportRealPerf
     */
    @ApiModelProperty(value = "supportRealPerf")
    private String supportRealPerf;

    /**
     * 私有ip
     */
    @ApiModelProperty(value = "私有ip")
    private String privateIps;



    /**
     * 型号-cpu
     */
    @ApiModelProperty(value = "型号-cpu")
    private String flavorCpu;


    /**
     * 型号-硬盘
     */
    @ApiModelProperty(value = "型号-硬盘")
    private String flavorDisk;


    /**
     * 型号-内存
     */
    @ApiModelProperty(value = "型号-内存")
    private String flavorMemory;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private String tenantId;

}

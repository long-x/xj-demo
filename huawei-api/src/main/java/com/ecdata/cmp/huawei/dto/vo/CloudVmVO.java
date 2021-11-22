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
 * @date ：Created in 2020/5/8 11:29
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "弹性云创建对象", description = "弹性云创建对象")
public class CloudVmVO {


    @ApiModelProperty(value = "待创建云服务器所在的可用分区")
    private String availabilityZone;

    @ApiModelProperty(value = "云服务器名称")
    private String name;

    @ApiModelProperty(value = "镜像ID")
    private String imageRef;

    @ApiModelProperty(value = "云服务器对应系统盘相关配置")
    private String rootVolume;

    /**
     * 云服务器系统盘对应的磁盘类型，需要与系统所提供的磁盘类型相匹配。
     * SATA：普通IO磁盘类型。 SAS：高IO磁盘类型。 SSD：超高IO磁盘类型。 co-p1：高IO (性能优化Ⅰ型) uh-l1：超高IO (时延优化) 说明 对于HANA云服务器、HL1型云服务器、HL2型云服务器，需使用co-p1和uh-l1两种磁盘类型。对于其他类型的云服务器，不能使用co-p1和uh-l1两种磁盘类型。
     */
    @ApiModelProperty(value = "磁盘类型")
    private String volumetype;

    /**
     * 磁盘大小
     */
    @ApiModelProperty(value = "磁盘大小")
    private String size;

    @ApiModelProperty(value = "规格的ID")
    private String flavorRef;

    @ApiModelProperty(value = "创建VPC的ID")
    private String vpcid;


    /**
     *云服务器对应安全组信息。
     * 约束：当该值指定为空时，默认给云服务器绑定default安全组。
     */
    @ApiModelProperty(value = "云服务器对应安全组信息")
    private String securityGroups;

    @ApiModelProperty(value = "需要指定已有安全组的ID")
    private String id;

    /**
     * 约束：
     1.网卡对应的网络（network）必须属于vpcid对应的VPC。
     2.当前单个云服务器支持最多挂载12张网卡。
     */
    @ApiModelProperty(value = "网卡信息")
    private String nics;

    /**
     * 待创建云服务器的网卡信息。需要指定vpcid对应VPC下已创建的网络（network）的ID，UUID格式
     */
    @ApiModelProperty(value = "VPC下已创建的网络")
    private String subnetId;


    /**
     自动分配，需要指定新创建弹性IP的信息。
     使用已有，需要指定已创建弹性IP的信息。
     */
    @ApiModelProperty(value = "配置云服务器的弹性IP信息")
    private String publicip;


    @ApiModelProperty(value = "配置云服务器的弹性IP信息")
    private String adminPass;

    @ApiModelProperty(value = "配置云服务器的弹性IP信息")
    private String count;


    @ApiModelProperty(value = "项目id")
    private String projectId;


    @ApiModelProperty(value = "虚拟机ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "Eip")
    private String eipId;


}

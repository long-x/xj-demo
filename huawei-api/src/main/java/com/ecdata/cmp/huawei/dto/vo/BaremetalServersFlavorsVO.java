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
 * @author ：xuj
 * @date ：Created in 2020/4/1 15:06
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "裸金属规格返回对象", description = "裸金属规格返回对象")
public class BaremetalServersFlavorsVO {


    @ApiModelProperty(value = "规格id")
    private String id;

    @ApiModelProperty(value = "规格名称")
    private String name;

    @ApiModelProperty(value = "cpu core")
    private String vcpus;

    @ApiModelProperty(value = "内存 M")
    private String ram;

    @ApiModelProperty(value = "磁盘 G")
    private String disk;

    @ApiModelProperty(value = "规格相关快捷链接地址")
    private List<Map> links;

    //详细信息
    @ApiModelProperty(value = "磁盘物理规格描述信息")
    private String diskDetail;

    @ApiModelProperty(value = "deploytype")
    private String deploytype;

    @ApiModelProperty(value = "标示ironic类型的flavor，避免调度器影响虚拟机调度")
    private String hypervisorType;

    @ApiModelProperty(value = "裸金属服务器的cpu架构类型例如:x86_64")
    private String cpuArch;

    @ApiModelProperty(value = "裸金属服务器规格类型，对应与下面参数的具体规格，如果下面参数的有差异，就需要独立出不同的board_type")
    private String boardType;

    @ApiModelProperty(value = "extBootType")
    private String extBootType;

    @ApiModelProperty(value = "实际可挂载网络数量")
    private String netNum;

    @ApiModelProperty(value = "网卡物理规格描述信息")
    private String netcardDetail;

    @ApiModelProperty(value = "cpu物理规格描述信息")
    private String cpuDetail;

    @ApiModelProperty(value = "裸金属服务器使用的资源类型，值为ironic")
    private String resourceType;

    @ApiModelProperty(value = "内存物理规格描述信息")
    private String memoryDetail;


}

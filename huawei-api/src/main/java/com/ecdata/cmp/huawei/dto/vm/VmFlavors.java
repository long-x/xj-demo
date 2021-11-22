package com.ecdata.cmp.huawei.dto.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description:虚拟机规格列表
 * @date 2020/1/13 15:47
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "虚拟机规格列表", description = "虚拟机规格列表")
public class VmFlavors implements Serializable {
    @ApiModelProperty(value = "云服务器规格名称")
    private String name;

    @ApiModelProperty(value = "该云服务器规格对应的内存大小，单位为MB")
    private String ram;

    @ApiModelProperty(value = "该云服务器规格对应的CPU核数")
    private String vcpus;

    @ApiModelProperty(value = "该云服务器规格对应要求系统盘大小，0为不限制。 此字段在本系统中无效")
    private String disk;

    @ApiModelProperty(value = "云服务器规格ID")
    private String id;
}

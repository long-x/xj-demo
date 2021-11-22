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
 * @date ：Created in 2020/4/2 10:44
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "弹性云规格返回对象", description = "弹性云规格返回对象")
public class CloudServersFlavorsVO {



    @ApiModelProperty(value = "规格id")
    private String id;

    @ApiModelProperty(value = "规格名称")
    private String name;

    @ApiModelProperty(value = "cpu core")
    private String vcpus;

    @ApiModelProperty(value = "内存 M")
    private String ram;

    @ApiModelProperty(value = "云服务器规格的分类")
    private String performanceType;

    @ApiModelProperty(value = "资源类型")
    private String resourceType;

    @ApiModelProperty(value = "extBootType")
    private String extBootType;



}

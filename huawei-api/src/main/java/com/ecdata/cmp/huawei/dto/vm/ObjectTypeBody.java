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
 * @description
 * @date 2019/12/3 16:33
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "监控对象", description = "监控对象")
public class ObjectTypeBody implements Serializable {
    private static final long serialVersionUID = 8229170517521200750L;
    /**
     * 监控对象类型编号
     */
    @ApiModelProperty(value = "监控对象类型编号")
    private String obj_type_id;

    /**
     * 父监控对象类型编号
     */
    @ApiModelProperty(value = "父监控对象类型编号")
    private String parent_obj_type_id;


    /**
     * 资源CI
     */
    @ApiModelProperty(value = "资源CI")
    private String resource_category;

    /**
     * 资源提供者
     */
    @ApiModelProperty(value = "资源提供者")
    private String resource_provider;

    /**
     * 英文描述
     */
    @ApiModelProperty(value = "英文描述")
    private String en_us;

    /**
     * 中文描述
     */
    @ApiModelProperty(value = "中文描述")
    private String zh_cn;

    /**
     * 资源类型组英文描述
     */
    @ApiModelProperty(value = "资源类型组英文描述")
    private String group_en_us;

    /**
     * 资源类型组中文描述
     */
    @ApiModelProperty(value = "资源类型组中文描述")
    private String group_zh_cn;
}

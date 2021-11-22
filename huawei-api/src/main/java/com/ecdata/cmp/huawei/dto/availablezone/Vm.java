package com.ecdata.cmp.huawei.dto.availablezone;

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
 * @date 2019/12/3 19:42
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "虚拟机状态", description = "虚拟机状态")
public class Vm implements Serializable {
    private static final long serialVersionUID = 676586926582172247L;
    /**
     * 容量信息是否为空
     */
    @ApiModelProperty(value = "容量信息是否为空")
    private String isDataEmpty;

    /**
     * 运行状态虚拟机数量
     */
    @ApiModelProperty(value = "运行状态虚拟机数量")
    private Integer running;

    /**
     * 停机状态虚拟机数量
     */
    @ApiModelProperty(value = "停机状态虚拟机数量")
    private Integer stopped;

    /**
     * 休眠状态虚拟机数量
     */
    @ApiModelProperty(value = "休眠状态虚拟机数量")
    private Integer hibernated;

    /**
     * 其他状态虚拟机数量
     */
    @ApiModelProperty(value = "其他状态虚拟机数量")
    private Integer other;

    /**
     * 虚拟机采集时间
     */
    @ApiModelProperty(value = "虚拟机采集时间")
    private String collectTime;

    /**
     * 更新时间周期
     */
    @ApiModelProperty(value = "更新时间周期")
    private Long updateTime;
}

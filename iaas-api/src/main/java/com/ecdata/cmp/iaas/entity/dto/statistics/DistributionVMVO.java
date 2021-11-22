package com.ecdata.cmp.iaas.entity.dto.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/15 14:19
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "已分配虚拟机信息", description = "已分配虚拟机信息")
public class DistributionVMVO {

    /**
     * vcdID
     */
    @ApiModelProperty(value = "vcdID")
    private Long id;

    /**
     * vdc 名称
     */
    @ApiModelProperty(value = "名称")
    private String remark;

    /**
     * 虚拟机数量
     */
    @ApiModelProperty(value = "虚拟机数量")
    private Long vmcon;

    /**
     * cpu
     */
    @ApiModelProperty(value = "cpu")
    private Long cpu;

    /**
     * 内存
     */
    @ApiModelProperty(value = "memory")
    private Long memory;

    /**
     * 存储
     */
    @ApiModelProperty(value = "disk")
    private Long disk;



}

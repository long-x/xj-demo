package com.ecdata.cmp.iaas.entity.dto.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/15 11:35
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "云平台资源统计 在途 cpu 内存 磁盘", description = "云平台资源统计 在途 cpu 内存 磁盘")
public class InTransitCMDVO {

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

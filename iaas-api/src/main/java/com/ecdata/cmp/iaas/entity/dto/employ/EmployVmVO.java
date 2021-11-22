package com.ecdata.cmp.iaas.entity.dto.employ;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/16 19:50
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "计算资源虚拟机信息", description = "计算资源虚拟机信息")
public class EmployVmVO {

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String remark;

    /**
     * cpu
     */
    @ApiModelProperty(value = "cpu")
    private Long cpu;

    /**
     * memory
     */
    @ApiModelProperty(value = "memory")
    private Long memory;

    /**
     * disk
     */
    @ApiModelProperty(value = "disk")
    private Long disk;

    /**
     * 剩余数
     */
    @ApiModelProperty(value = "areaSum")
    private Long areaSum;
}

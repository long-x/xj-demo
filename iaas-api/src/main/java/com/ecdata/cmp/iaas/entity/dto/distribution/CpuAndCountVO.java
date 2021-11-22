package com.ecdata.cmp.iaas.entity.dto.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/5 14:36
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "获取其他信息", description = "获取其他信息")
public class CpuAndCountVO {

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "区域")
    private String remark;

    @ApiModelProperty(value = "cpu")
    private String vcpu;

    @ApiModelProperty(value = "内存")
    private String memory;

    @ApiModelProperty(value = "虚拟机数量")
    private String vmcon;

    @ApiModelProperty(value = "磁盘")
    private String disk;

    @ApiModelProperty(value = "裸金属型号")
    private String detail;

    @ApiModelProperty(value = "裸金属数量")
    private String ibmcon;

}

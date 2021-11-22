package com.ecdata.cmp.iaas.entity.dto.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/12 13:49
 */
@Data
public class AreaInfoVO {
    private Long id;
    @ApiModelProperty("可用分区")
    private String area;

    @ApiModelProperty("cpu")
    private int cpu;

    @ApiModelProperty("memory")
    private int memory;

    @ApiModelProperty("disk")
    private int disk;

    @ApiModelProperty("系统数量")
    private int vmNum;

    @ApiModelProperty("裸金属")
    private List<BareMetalAssignedVO> bareMetalVOS;
}

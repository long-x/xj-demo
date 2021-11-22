package com.ecdata.cmp.iaas.entity.dto.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/5/12 9:20
 */
@Data
public class BareMetalAssignedVO {
    private Long id;
    @ApiModelProperty("已分配裸金属型号")
    private String bareMetalName;

    @ApiModelProperty("裸金属数量")
    private long bareMetal;
}

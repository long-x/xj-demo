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
 * @date ：Created in 2019/12/12 11:25
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机对象-Condition", description = "虚拟机对象-Condition")
public class VirtualMachineConditionVO {

    @ApiModelProperty(value = "resId")
    private String resId;

    @ApiModelProperty(value = "regionId")
    private String regionId;

    @ApiModelProperty(value = "bizRegionId")
    private String bizRegionId;
}

package com.ecdata.cmp.huawei.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "超分前的实际容量信息", description = "超分前的实际容量信息")
public class ActualCapacity implements Serializable {
    private static final long serialVersionUID = -8768365926433017560L;
    /**
     * 总容量
     */
    @ApiModelProperty(value = "总容量")
    private TotalCapacity totalCapacity;

    /**
     * 已使用容量
     */
    @ApiModelProperty(value = "已使用容量")
    private UsedCapacity usedCapacity;

    /**
     * 剩余容量
     */
    @ApiModelProperty(value = "剩余容量")
    private FreeCapacity freeCapacity;
}

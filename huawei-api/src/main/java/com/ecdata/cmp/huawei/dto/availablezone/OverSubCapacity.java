package com.ecdata.cmp.huawei.dto.availablezone;

import com.ecdata.cmp.huawei.dto.region.TotalCapacity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "OverSubCapacity对象", description = "OverSubCapacity对象")
public class OverSubCapacity implements Serializable {
    /**
     * 超分比
     */
    @ApiModelProperty(value = "超分比")
    private Float oversubscriptionRatio;

    /**
     * 超分后总虚拟容量
     */
    @ApiModelProperty(value = "超分后总虚拟容量")
    private TotalCapacity totalCapacity;

    /**
     * 超分后已分配的虚拟容量
     */
    @ApiModelProperty(value = "超分后已分配的虚拟容量")
    private AtomicCapacity allocatedCapacity;

    /**
     * 超分后剩余的虚拟容量
     */
    @ApiModelProperty(value = "超分后剩余的虚拟容量")
    private AtomicCapacity freeCapacity;

    public String total() {
        if (totalCapacity == null) {
            return "";
        }
        return totalCapacity.getCapacityValue();
    }

    public String used() {
        if (allocatedCapacity == null) {
            return "";
        }
        return allocatedCapacity.getCapacityValue();
    }
}

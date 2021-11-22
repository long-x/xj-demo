package com.ecdata.cmp.huawei.dto.availablezone;

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
@ApiModel(value = "AtomicCapacity对象", description = "AtomicCapacity对象")
public class AtomicCapacity implements Serializable {
    /**
     * 容量信息是否为空
     */
    @ApiModelProperty(value = "容量的统一单位")
    private String unit;

    /**
     * 容量按照统一单位计量时的容量数值
     */
    @ApiModelProperty(value = "容量按照统一单位计量时的容量数值 cpu或内存已使用")
    private String capacityValue;

    /**
     * 容量按照容量值大小自适应取对应容量单位时的含单位容量内容
     */
    @ApiModelProperty(value = "容量按照容量值大小自适应取对应容量单位时的含单位容量内容")
    private String displayCapacity;

    /**
     * 容量占总容量的比例
     */
    @ApiModelProperty(value = "容量占总容量的比例")
    private Double ratio;
}
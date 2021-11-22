package com.ecdata.cmp.huawei.dto.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/12/3 20:44
 */
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AllocatedCapacity对象", description = "AllocatedCapacity对象")
public class AllocatedCapacity implements Serializable {

    private static final long serialVersionUID = 8023570996855862807L;
    /**
     * 容量信息是否为空
     */
    @ApiModelProperty(value = "容量的统一单位")
    private String unit;

    /**
     * 容量按照统一单位计量时的容量数值
     */
    @ApiModelProperty(value = "容量按照统一单位计量时的容量数值")
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

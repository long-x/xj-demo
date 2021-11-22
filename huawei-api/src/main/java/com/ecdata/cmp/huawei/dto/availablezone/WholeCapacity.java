package com.ecdata.cmp.huawei.dto.availablezone;

import com.ecdata.cmp.huawei.dto.region.ActualCapacity;
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
@ApiModel(value = "按维度总容量", description = "按维度总容量")
public class WholeCapacity implements Serializable {

    private static final long serialVersionUID = -3962904946096327041L;
    /**
     * 标记容量信息是否为空
     */
    @ApiModelProperty(value = "标记容量信息是否为空")
    private String isDataEmpty;

    /**
     * 超分容量
     */
    @ApiModelProperty(value = "超分容量")
    private OverSubCapacity oversubscriptionCapacity;

    /**
     * 实际容量
     */
    @ApiModelProperty(value = "实际容量")
    private ActualCapacity actualCapacity;

    /**
     * 采集时间周期
     */
    @ApiModelProperty(value = "采集时间周期")
    private String collectTime;

    /**
     * 更新时间周期
     */
    @ApiModelProperty(value = "更新时间周期")
    private String updateTime;

    public String total() {
        if (oversubscriptionCapacity == null) {
            return "";
        }
        return oversubscriptionCapacity.total();
    }

    public String used() {
        if (oversubscriptionCapacity == null) {
            return "";
        }
        return oversubscriptionCapacity.used();
    }
}

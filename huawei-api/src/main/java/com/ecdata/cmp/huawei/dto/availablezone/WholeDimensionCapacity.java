package com.ecdata.cmp.huawei.dto.availablezone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "按维度统计信息", description = "按维度统计信息")
public class WholeDimensionCapacity implements Serializable {

    private static final long serialVersionUID = -3442153379104024374L;

    /**
     * 按维度总容量
     */
    @ApiModelProperty(value = "按维度总容量 TB")
    private WholeCapacity capacity;

    /**
     * 维度
     */
    @ApiModelProperty(value = "维度")
    private List<DimensionInfo> dimensions;

    public double diskTotal() {
        if (capacity == null) {
            return 0;
        }
        return StringUtils.isBlank(capacity.total()) ? 0 : Double.valueOf(capacity.total());
    }

    public double diskUsed() {
        if (capacity == null) {
            return 0;
        }
        return StringUtils.isBlank(capacity.used()) ? 0 : Double.valueOf(capacity.used());
    }
}

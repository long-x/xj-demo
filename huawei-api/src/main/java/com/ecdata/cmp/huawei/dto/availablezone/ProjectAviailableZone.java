package com.ecdata.cmp.huawei.dto.availablezone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description:
 * @date 2020/1/15 14:31
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AtomicCapacity对象", description = "AtomicCapacity对象")
public class ProjectAviailableZone implements Serializable {
    private static final long serialVersionUID = 2660545180819913232L;
    /**
     * 可用分区是否可用
     */
    @ApiModelProperty(value = "可用分区是否可用")
    private String zoneState;

    /**
     * 可用分区的名字
     */
    @ApiModelProperty(value = "可用分区的名字")
    private String zoneName;
}

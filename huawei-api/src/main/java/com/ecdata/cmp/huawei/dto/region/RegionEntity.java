package com.ecdata.cmp.huawei.dto.region;

import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "iaas模块Region", description = "iaas模块Region")
public class RegionEntity implements Serializable {
    private static final long serialVersionUID = -3473902253996799367L;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String id;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String name;

    /**
     * 资源池
     */
    @ApiModelProperty(value = "资源池")
    private ResourcePool resourcePool;

    public List<AvailableZone> availableZones() {
        if (this == null || this.resourcePool == null) {
            return null;
        }
        return this.resourcePool.getAvailableZoneList();
    }

}

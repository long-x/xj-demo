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
@ApiModel(value = "ResourcePool", description = "ResourcePool")
public class ResourcePool implements Serializable {
    /**
     * 资源池id
     */
    @ApiModelProperty(value = "资源池id")
    private String id;

    /**
     * 资源池名称
     */
    @ApiModelProperty(value = "资源池名称")
    private String name;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "可用分区列表")
    private List<AvailableZone> availableZoneList;

    @ApiModelProperty(value = "预留的分配的虚拟cpu数量")
    private Integer vcpuReservedAllocate;

    @ApiModelProperty(value = "预留的分配的内存大小(MB)")
    private Integer memoryReservedAllocate;

    @ApiModelProperty(value = "预留的分配的虚拟机数量")
    private Integer vmReservedAllocate;

    @ApiModelProperty(value = "预留的分配的pod数量")
    private Integer podReservedAllocate;

}

package com.ecdata.cmp.huawei.dto.availablezone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "可用区资源统计", description = "可用区资源统计")
public class AvailableZoneResource implements Serializable {
    private static final long serialVersionUID = -4541806294757729297L;
    /**
     * 可用分区机器状态
     */
    @ApiModelProperty(value = "可用分区机器状态")
    private AvailableZoneStatistics availableZoneStatistics;

    /**
     * vCPU统计
     */
    @ApiModelProperty(value = "vCPU统计")
    private WholeCapacity vCPU;

    /**
     * 内存统计
     */
    @ApiModelProperty(value = "内存统计")
    private WholeCapacity memory;

    /**
     * 磁盘统计
     */
    @ApiModelProperty(value = "磁盘统计")
    private List<WholeDimensionCapacity> capacityList;

    public int vmNum() {
        if (availableZoneStatistics == null) {
            return 0;
        }
        return availableZoneStatistics.vmNum();
    }

    public String cpuTotal() {
        if (vCPU == null) {
            return "";
        }
        return vCPU.total();
    }

    public String cpuUsed() {
        if (vCPU == null) {
            return "";
        }
        return vCPU.used();
    }

    public String memoryTotal() {
        if (memory == null) {
            return "";
        }
        return memory.total();
    }

    public String memoryUsed() {
        if (memory == null) {
            return "";
        }
        return memory.used();
    }

    public double diskTotal() {
        if (CollectionUtils.isEmpty(capacityList)) {
            return 0;
        }
        int total = 0;
        for (WholeDimensionCapacity capacity : capacityList) {
            total += capacity.diskTotal();
        }
        return total;
    }

    public double diskUsed() {
        if (CollectionUtils.isEmpty(capacityList)) {

            return 0;
        }

        int used = 0;
        for (WholeDimensionCapacity capacity : capacityList) {
            used += capacity.diskUsed();
        }
        return used;
    }
}

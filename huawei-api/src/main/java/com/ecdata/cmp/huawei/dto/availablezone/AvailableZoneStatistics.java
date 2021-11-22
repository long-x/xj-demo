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
 * @description
 * @date 2019/12/3 20:19
 */
@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "可用区机器统计", description = "可用区机器统计")
public class AvailableZoneStatistics implements Serializable {

    private static final long serialVersionUID = 948852981555025533L;
    /**
     * 主机状态统计
     */
    @ApiModelProperty(value = "主机状态统计")
    private Host host;

    /**
     * 虚拟机状态统计
     */
    @ApiModelProperty(value = "虚拟机状态统计")
    private Vm vm;

    public int vmNum() {
        if (vm == null) {
            return 0;
        }

        return vm.getRunning();
    }
}

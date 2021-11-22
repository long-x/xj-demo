package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 业务组关联资源池
 */
@Data
public class BusinessGroupStatisticsVO {
    private Long id;
    private String businessGroupName;
    private Long parentId;
    private String isApp;
    private List<ResourcePoolStatisticsVO> poolStatisticsVOS;
    private List<UserResourceVO> userResourceVOS;
    private List<BusinessGroupStatisticsVO> child;

    public int cpuTotal() {
        if (CollectionUtils.isEmpty(poolStatisticsVOS)) {
            return 0;
        }

        return poolStatisticsVOS.stream().mapToInt(ResourcePoolStatisticsVO::getCpuTotal).sum();
    }

    public int memoryTotal() {
        if (CollectionUtils.isEmpty(poolStatisticsVOS)) {
            return 0;
        }

        return poolStatisticsVOS.stream().mapToInt(ResourcePoolStatisticsVO::getMemoryTotal).sum();
    }

    public int vmTotal() {
        if (CollectionUtils.isEmpty(poolStatisticsVOS)) {
            return 0;
        }

        return poolStatisticsVOS.stream().mapToInt(ResourcePoolStatisticsVO::getVmTotal).sum();
    }

    public int vmUsed() {
        if (CollectionUtils.isEmpty(poolStatisticsVOS)) {
            return 0;
        }

        return poolStatisticsVOS.stream().mapToInt(ResourcePoolStatisticsVO::getVmUsed).sum();
    }


}

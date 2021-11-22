package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

@Data
public class BusinessGroupResourceCapacityVO {
    private List<BusinessGroupResourceStatisticsVO> cpuTop5;

    private List<BusinessGroupResourceStatisticsVO> memoryTop5;
}

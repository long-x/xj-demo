package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

/**
 * 业务组云主机分布
 */
@Data
public class HostDistributionPieVO {
    private int sum;

    private List<HostDistributionPieDataVO> data;
}

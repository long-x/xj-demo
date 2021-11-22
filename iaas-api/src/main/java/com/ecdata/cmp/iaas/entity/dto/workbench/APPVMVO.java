package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

@Data
public class APPVMVO {
    private Long id;
    private String vmName;
    //业务组人员资源分布使用
    private List<APPResourceVO> appResourceVOList;

    //业务组云主机分布使用
    private List<HostDistributionPieDataNewVO> hostDistributionPieDataNewVOList;
}

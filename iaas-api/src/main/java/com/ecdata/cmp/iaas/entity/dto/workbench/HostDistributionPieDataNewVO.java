package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class HostDistributionPieDataNewVO {
    private Long id;
    private String name;
    private Long parentId;
    private List<APPVMVO> appvmvoList;
    private List<HostDistributionPieDataNewVO> children;

    public int vmSum() {
        if (this == null || CollectionUtils.isEmpty(this.getAppvmvoList())) {
            return 0;
        }
        return this.getAppvmvoList().size();
    }

}

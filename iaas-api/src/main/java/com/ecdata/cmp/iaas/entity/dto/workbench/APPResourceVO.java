package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

@Data
public class APPResourceVO {
    private Long id;
    private String businessGroupName;
    private List<APPVMVO> appvmvoList;
}

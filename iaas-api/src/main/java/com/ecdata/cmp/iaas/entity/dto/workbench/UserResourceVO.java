package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class UserResourceVO {
    private Long id;
    private String name;
    private List<BusinessGroupStatisticsVO> businessGroupStatisticsVOS;
}

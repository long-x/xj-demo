package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.HashMap;

/**
 * 业务组云主机申请趋势
 */
@Data
public class HostApplyCountVO {
    private String date;

    private HashMap<String, Integer> data;
}

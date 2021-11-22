package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

/**
 * 业务组人员资源分布
 */
@Data
public class UserBusinessGroupResourceVO {
    private String userName;
    private String groupName;
    private int resourceNum;
}

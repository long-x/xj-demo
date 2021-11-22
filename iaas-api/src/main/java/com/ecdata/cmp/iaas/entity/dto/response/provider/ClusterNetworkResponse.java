package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 10:03
 */
@Data
public class ClusterNetworkResponse {
    /**
     * 保存数据库后的id
     */
    private Long id;
    /**
     * 网络名
     */
    private String name;
    /**
     * 网络类型
     */
    private String type;
    /**
     * 网络关联唯一key
     */
    private String networkKey;
}

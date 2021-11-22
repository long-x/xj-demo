package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;

/**
 * 描述:资源池存储
 *
 * @author xxj
 * @create 2019-11-15 16:19
 */
@Data
public class ResourcePoolDatastoreResponse {
    /**
     * 分配存储总量(gb)
     */
    private Double spaceTotalAllocate;

    /**
     * 已用分配存储总量(gb)
     */
    private Double spaceUsedAllocate;
}

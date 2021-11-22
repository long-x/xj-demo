package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;

import java.util.List;

/**
 * 描述:资源池
 *
 * @author xxj
 * @create 2019-11-15 16:15
 */
@Data
public class ResourcePoolResponse {
    /**
     * 资源池名
     */
    private String poolName;

    /**
     * 标签(逗号分隔)
     */
    private String tag;

    /**
     * 分配的虚拟cpu数量
     */
    private Integer vcpuTotalAllocate;

    /**
     * 已使用分配的虚拟cpu数量
     */
    private Double vcpuUsedAllocate;

    /**
     * 分配的内存大小(mb)
     */
    private Integer memoryTotalAllocate;

    /**
     * 已使用分配的内存大小(mb)
     */
    private Double memoryUsedAllocate;

    /**
     * 分配的虚拟机数量
     */
    private Integer vmTotalAllocate;

    /**
     * 已使用分配的虚拟机数量
     */
    private Integer vmUsedAllocate;

    /**
     * 分配的pod数量
     */
    private Integer podTotalAllocate;

    /**
     * 已使用分配的pod数量
     */
    private Integer podUsedAllocate;

    /**
     * 资源池存储
     */
    private List<ResourcePoolDatastoreResponse> resourcePoolDatastoreList;

}

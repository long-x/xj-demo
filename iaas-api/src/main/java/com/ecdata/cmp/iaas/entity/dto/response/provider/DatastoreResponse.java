package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 描述:主机存储
 *
 * @author xxj
 * @create 2019-11-14 13:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatastoreResponse {

    /**
     * 存储名
     */
    private String datastoreName;
    /**
     * 存储类型
     */
    private String driveType;
    /**
     * 总空间
     */
    private Double spaceTotal;
    /**
     * 已用空间
     */
    private Double spaceUsed;
    /**
     * 关联唯一key
     */
    private String datastoreKey;

    /**
     * 资源池存储
     */
    private List<ResourcePoolDatastoreResponse> resourcePoolDatastoreList;

    /**
     * 虚拟机
     */
    private List<VirtualMachineResponse> virtualMachineResponseList;

    public int resourcePoolDatastoreList() {
        if (this == null || CollectionUtils.isEmpty(this.resourcePoolDatastoreList)) {
            return 0;
        }
        return this.resourcePoolDatastoreList.size();
    }

    /**
     * 资源池存储-分配存储总量(gb)
     *
     * @return
     */
    public double spaceTotalAllocate() {
        if (CollectionUtils.isEmpty(this.resourcePoolDatastoreList)) {
            return 0;
        }
        return this.resourcePoolDatastoreList.stream().mapToDouble(ResourcePoolDatastoreResponse::getSpaceTotalAllocate).sum();
    }

    /**
     * 资源池存储-已用分配存储总量(gb)
     *
     * @return
     */
    public double spaceUsedAllocate() {
        if (CollectionUtils.isEmpty(this.resourcePoolDatastoreList)) {
            return 0;
        }
        return this.resourcePoolDatastoreList.stream().mapToDouble(ResourcePoolDatastoreResponse::getSpaceUsedAllocate).sum();
    }
}

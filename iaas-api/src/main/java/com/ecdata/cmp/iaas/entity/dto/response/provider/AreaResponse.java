package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-14 13:37
 */
@Data
public class AreaResponse {

    /**
     * 区域名
     */
    private String areaName;

    /**
     * 区域关联唯一key
     */
    private String areaKey;

    /**
     * 集群
     */
    private List<ClusterResponse> clusterList;

    /**
     * 获取虚拟机数量
     */
    public int vmNum() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        int total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.getVmNum();
        }
        return total;
    }

    /**
     * 获取cpuTotal
     */
    public double cpuTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getCpuTotal() != null) {
                total += cluster.getCpuTotal();
            }
        }
        return total;
    }

    /**
     * 获取cpuUsed
     */
    public double cpuUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getCpuTotal() != null) {
                total += cluster.getCpuUsed();
            }
        }
        return total;
    }


    /**
     * 获取memoryTotal
     */
    public double memoryTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getMemoryTotal() != null) {
                total += cluster.getMemoryTotal();
            }
        }
        return total;
    }

    /**
     * 获取memoryUsed
     */
    public double memoryUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getMemoryTotal() != null) {
                total += cluster.getMemoryUsed();
            }
        }
        return total;
    }

    /**
     * 获取diskTotal
     */
    public double diskTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getDiskTotal() != null) {
                total += cluster.getDiskTotal();
            }
        }
        return total;
    }

    /**
     * 获取diskUsed
     */
    public double diskUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }
        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster.getDiskUsed() != null) {
                total += cluster.getDiskUsed();
            }
        }
        return total;
    }


    /**
     * 获取集群信息
     *
     * @return
     */
    public List<ClusterResponse> clusterList() {
        return this == null ? Collections.emptyList() : this.getClusterList();
    }

    /**
     * 获取主机信息
     *
     * @return
     */
    public List<HostResponse> hostList() {
        if (this == null || CollectionUtils.isEmpty(this.getClusterList())) {
            return Collections.emptyList();
        }

        List<HostResponse> hosts = new ArrayList<>();
        for (ClusterResponse cluster : this.clusterList) {
            if (cluster == null) {
                continue;
            }

            hosts.addAll(cluster.getHostList());
        }
        return hosts;
    }

    /**
     * 主机存储
     *
     * @return
     */
    public List<DatastoreResponse> hostDatastoreList() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return Collections.emptyList();
        }

        List<DatastoreResponse> datastores = new ArrayList<>();
        for (ClusterResponse cluster : this.clusterList) {
            datastores.addAll(cluster.hostDatastoreList());
        }

        return datastores;
    }

    /**
     * 主机cpu总量
     *
     * @return
     */
    public int hostCpuTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        int total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostCpuTotal();
        }
        return total;
    }

    /**
     * 主机cpu使用
     *
     * @return
     */
    public int hostCpuUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        int total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostCpuUsed();
        }
        return total;
    }


    /**
     * 主机内存总量
     *
     * @return
     */
    public double hostMemoryTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        int total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostMemoryTotal();
        }
        return total;
    }

    /**
     * 主机内存使用
     *
     * @return
     */
    public int hostMemoryUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        int total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostMemoryUsed();
        }
        return total;
    }

    /**
     * 主机存储-总空间
     *
     * @return
     */
    public double hostDatastoreSpaceTotal() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostDatastoreSpaceTotal();
        }
        return total;
    }

    /**
     * 主机存储-已用空间
     *
     * @return
     */
    public double hostDatastoreSpaceUsed() {
        if (this == null || CollectionUtils.isEmpty(this.clusterList)) {
            return 0;
        }

        double total = 0;
        for (ClusterResponse cluster : this.clusterList) {
            total += cluster.hostDatastoreSpaceUsed();
        }
        return total;
    }
}

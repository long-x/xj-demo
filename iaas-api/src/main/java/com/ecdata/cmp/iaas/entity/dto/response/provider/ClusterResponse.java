package com.ecdata.cmp.iaas.entity.dto.response.provider;

import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import io.swagger.annotations.ApiModelProperty;
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
public class ClusterResponse {
    /**
     * 集群名
     */
    private String clusterName;
    /**
     * 集群关联唯一key
     */
    private String clusterKey;

    /**
     * 虚拟机总量
     */
    private int vmNum;

    /**
     * 总cpu频率
     */
    @ApiModelProperty(value = "总cpu频率")
    private Double cpuTotal;

    /**
     * 已用cpu频率
     */
    @ApiModelProperty(value = "已用cpu频率")
    private Double cpuUsed;

    /**
     * 磁盘总量
     */
    @ApiModelProperty(value = "磁盘总量")
    private Double diskTotal;

    /**
     * 已用磁盘总量
     */
    @ApiModelProperty(value = "已用磁盘总量")
    private Double diskUsed;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryTotal;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryUsed;

    /**
     * 主机
     */
    private List<HostResponse> hostList;

    /**
     * 网络
     */
    private List<ClusterNetworkResponse> clusterNetworkList;

    /**
     * 主机与网络关系
     */
    private List<NetworkResponse> networkList;

    /**
     * 资源池
     */
    private ResourcePoolResponse resourcePool;

    /**
     * vdc信息
     */
    private List<IaasVirtualDataCenterVO> virtualDataCenterVOList;

    /**
     * 主机存储
     *
     * @return
     */
    public List<DatastoreResponse> hostDatastoreList() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return Collections.emptyList();
        }

        List<DatastoreResponse> datastores = new ArrayList<>();
        for (HostResponse host : this.hostList) {
            datastores.addAll(host.hostDatastoreList());
        }

        return datastores;
    }

    /**
     * 主机cpu总量
     *
     * @return
     */
    public double hostCpuTotal() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        return this.hostList.stream().mapToDouble(HostResponse::getCpuTotal).sum();
    }

    /**
     * 主机cpu使用
     *
     * @return
     */
    public double hostCpuUsed() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        return this.hostList.stream().mapToDouble(HostResponse::getCpuUsed).sum();
    }


    /**
     * 主机内存总量
     *
     * @return
     */
    public double hostMemoryTotal() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        return this.hostList.stream().mapToDouble(HostResponse::getMemoryTotal).sum();
    }

    /**
     * 主机内存使用
     *
     * @return
     */
    public double hostMemoryUsed() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        return this.hostList.stream().mapToDouble(HostResponse::getMemoryUsed).sum();
    }


    /**
     * 主机存储-总空间
     *
     * @return
     */
    public double hostDatastoreSpaceTotal() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        double total = 0;
        for (HostResponse host : this.hostList) {
            total += host.hostDatastoreSpaceTotal();
        }
        return total;
    }

    /**
     * 主机存储-已用空间
     *
     * @return
     */
    public double hostDatastoreSpaceUsed() {
        if (this == null || CollectionUtils.isEmpty(this.hostList)) {
            return 0;
        }

        double total = 0;
        for (HostResponse host : this.hostList) {
            total += host.hostDatastoreSpaceUsed();
        }
        return total;
    }
}

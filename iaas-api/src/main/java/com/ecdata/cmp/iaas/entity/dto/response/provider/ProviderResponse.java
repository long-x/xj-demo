package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:供应商
 *
 * @author xxj
 * @create 2019-11-15 14:09
 */
@Data
public class ProviderResponse {

    private String version;
    private String providerName;
    private List<AreaResponse> areaList;

    public List<ClusterResponse> clusters() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return Collections.emptyList();
        }

        List<ClusterResponse> clusterList = new ArrayList<>();
        for (AreaResponse area : this.areaList) {
            clusterList.addAll(area.getClusterList());
        }
        return clusterList;
    }

    /**
     * 获取虚拟机数量
     */
    public int vmNum() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        int total = 0;
        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.vmNum();
        }
        return total;
    }

    /**
     * 主机个数
     */
    public int hostNum() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        List<HostResponse> hostResponses = new ArrayList<>();
        for (AreaResponse areaResponse : this.areaList) {
            hostResponses.addAll(areaResponse.hostList());
        }

        return hostResponses.size();
    }

    public double cpuTotal() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.cpuTotal();
        }
        return total;
    }

    public double cpuUsed() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.cpuUsed();
        }
        return total;
    }

    public double memoryTotal() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.memoryTotal();
        }
        return total;
    }

    public double memoryUsed() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.memoryUsed();
        }
        return total;
    }

    public double diskTotal() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.diskTotal();
        }
        return total;
    }

    public double diskUsed() {
        if (this == null || CollectionUtils.isEmpty(this.areaList)) {
            return 0;
        }
        double total = 0;

        for (AreaResponse areaResponse : this.areaList) {
            total += areaResponse.diskUsed();
        }
        return total;
    }

}

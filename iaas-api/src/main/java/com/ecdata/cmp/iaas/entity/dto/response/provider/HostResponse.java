package com.ecdata.cmp.iaas.entity.dto.response.provider;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 描述:主机
 *
 * @author xxj
 * @create 2019-11-14 13:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostResponse {
    /**
     * 主机名
     */
    private String hostName;

    /**
     * 主机cpu总量
     */
    private int cpuTotal;

    /**
     * 主机cpu使用
     */
    private int cpuUsed;

    /**
     * 主机内存总量
     */
    private int memoryTotal;
    /**
     * 主机内存使用
     */
    private int memoryUsed;

    /**
     * 磁盘总量(GB)
     */
    private double diskTotal;

    /**
     * 磁盘使用(GB)
     */
    private double diskUsed;

    /**
     * 主机虚拟cpu核数
     */
    private String vcpu;
    /**
     * 主机数据中心
     */
    private String dataCenter;
    /**
     * exsi build号
     */
    private String exsiBuildNumber;
    /**
     * exsi详细版本
     */
    private String exsiFullVersion;
    /**
     * exsi版本
     */
    private String exsiVersion;
    /**
     * 主机代理api版本
     */
    private String hostAgentApiVersion;
    /**
     * 逻辑处理器
     */
    private String logicProcessor;
    /**
     * moref id
     */
    private String morefId;
    /**
     * 虚拟网卡
     */
    private String nics;
    /**
     * 逻辑处理器
     */
    private String processorSocket;
    /**
     * 启用vnc
     */
    private String enabled;
    /**
     * 起始端口
     */
    private String fromPort;
    /**
     * 结束端口
     */
    private String toPort;
    /**
     * 代理地址
     */
    private String proxyAddress;
    /**
     * 关联唯一key
     */
    private String hostKey;

    /**
     * 主机存储
     */
    private List<DatastoreResponse> datastoreList;

    /**
     * 主机下的网络
     */
    private List<ClusterNetworkResponse> hostNetworkList;

    /**
     * 主机存储
     *
     * @return
     */
    public List<DatastoreResponse> hostDatastoreList() {
        if (CollectionUtils.isEmpty(this.datastoreList)) {
            return Collections.emptyList();
        }

        return this.datastoreList;
    }

    /**
     * 主机存储-总空间
     *
     * @return
     */
    public double hostDatastoreSpaceTotal() {
        if (CollectionUtils.isEmpty(this.datastoreList)) {
            return 0;
        }
        return this.datastoreList.stream().mapToDouble(DatastoreResponse::getSpaceTotal).sum();
    }

    /**
     * 主机存储-已用空间
     *
     * @return
     */
    public double hostDatastoreSpaceUsed() {
        if (CollectionUtils.isEmpty(this.datastoreList)) {
            return 0;
        }
        return this.datastoreList.stream().mapToDouble(DatastoreResponse::getSpaceUsed).sum();
    }
}

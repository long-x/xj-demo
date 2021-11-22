package com.ecdata.cmp.iaas.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 描述:集群和主机概况
 *
 * @author xxj
 * @create 2019-11-15 11:25
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "集群和主机概况 对象", description = "集群和主机概况 对象")
public class ClusterAndHostOverviewVO {
    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 所属数据中心
     */
    private String clusterDataCenter;

    /**
     * 资源池总数
     */
    private Integer resourcePoolTotal;

    /**
     * 已部署虚机个数
     */
    private Integer virtualDeployed;

    /**
     * 预留虚机个数
     */
    private Integer virtualReserved;

    /**
     * 集群cpu预留
     */
    private Double clusterCpuReserved;

    /**
     * 集群cpu总量
     */
    private Double clusterCpuTotal;

    /**
     * 集群内存预留
     */
    private Double clusterMemoryReserved;

    /**
     * 集群内存总量
     */
    private Double clusterMemoryTotal;

    /**
     * 集群存储预留
     */
    private Double clusterSpaceReserved;

    /**
     * 集群存储总量
     */
    private Double clusterSpaceTotal;

    /**
     * 主机概况
     */
    private List<HostOverviewVO> hostOverviewList;

}

package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 描述:容器镜像资源
 *
 * @author xxj
 * @create 2019-11-15 11:20
 */
@Data
public class ContainerImageResourceVO {

    //资源统计
    private ResourceStatisticsVO resourceStatistics;

    //数据中心概况
    private List<DataCenterOverviewVO> dataCenterOverviewVOList;

    //集群和主机概况
    private List<ClusterAndHostOverviewVO> clusterAndHostOverview;

    //数据存储
    private List<DataDatastoreVO> dataDatastoreVOList;
}

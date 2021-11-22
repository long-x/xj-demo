package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 描述:华为资源信息
 *
 * @author xxj
 * @create 2019-11-15 11:20
 */
@Data
public class HuaWeiContainerImageResourceVO {

    //数据中心概况
    private DataCenterOverviewVO dataCenterOverviewVO;

    //集群和宿主机概况
    private List<ClusterAndHostOverviewVO> clusterAndHostOverview;

    //vdc信息
    private List<IaasVirtualDataCenterVO> dataCenterVOList;
}

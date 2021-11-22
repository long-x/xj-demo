package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

/**
 * 描述:数据中心概况
 *
 * @author xxj
 * @create 2019-11-15 11:23
 */
@Data
public class DataCenterOverviewVO {

    /**
     * 数据中心名称
     */
    private String areaName;

    /**
     * 群集个数
     */
    private Integer clusterNum;

    /**
     * 主机个数
     */
    private Integer hostNum;

    /**
     * 虚拟机个数
     */
    private Integer virtualNum;

    /**
     * CPU总量
     */
    private double cpuTotal;

    /**
     * CPU已用
     */
    private double cpuUsed;

    /**
     * 内存总量
     */
    private double memoryTotal;

    /**
     * 内存已用
     */
    private double memoryUsed;

    /**
     * 存储总量
     */
    private double datastoreTotal;

    /**
     * 存储已用
     */
    private double datastoreUsed;
}

package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

/**
 * 描述:容器资源统计
 *
 * @author xxj
 * @create 2019-11-15 11:21
 */
@Data
public class ResourceStatisticsVO {

    /**
     * 版本号
     */
    private String version;

    /**
     * 物理主机个数
     */
    private Integer hostNum;

    /**
     * 群集个数
     */
    private Integer clusterNum;

    /**
     * 数据存储个数
     */
    private Integer datastoreNum;

    /**
     * 标准交换机个数
     */
    private Integer networkNum;

    /**
     * 虚拟机个数
     */
    private Integer virtualNum;

}

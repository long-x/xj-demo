package com.ecdata.cmp.iaas.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ty
 * @description
 * @date 2019/11/16 20:47
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "主机概况 对象", description = "主机概况 对象")
public class HostOverviewVO {
    /**
     * 主机名称
     */
    private String hostNname;

    /**
     * 主机型号
     */
    private String hostType;

    /**
     * 主机所属数据中心
     */
    private String hostDataCenter;

    /**
     * 主机cpu已用
     */
    private Integer hostCpuUsed;

    /**
     * 主机cpu总量
     */
    private Integer hostCpuTotal;

    /**
     * 主机内存已用
     */
    private Integer hostMemoryUsed;

    /**
     * 主机内存总量
     */
    private Integer hostMemoryTotal;

    /**
     * 主机存储已用
     */
    private Double hostSpaceUsed;

    /**
     * 主机存储总量
     */
    private Double hostSpaceTotal;
}

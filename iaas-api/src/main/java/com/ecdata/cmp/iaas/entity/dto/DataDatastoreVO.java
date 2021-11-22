package com.ecdata.cmp.iaas.entity.dto;

import lombok.Data;

/**
 * 描述:数据存储概况
 *
 * @author xxj
 * @create 2019-11-15 11:26
 */
@Data
public class DataDatastoreVO {

    /**
     * 名称
     */
    private String datastoreName;

    /**
     * 类型
     */
    private String driveType;

    /**
     * 总量
     */
    private double spaceTotal;

    /**
     * 已用
     */
    private double spaceUsed;

    /**
     * 分配(预留)存储总量(GB)
     */
    private double spaceTotalAllocate;

    /**
     * 存储总量(GB)
     */
    private double spaceUsedAllocate;

    /**
     * 所属数据中心
     */
    private String areaName;

    /**
     * 资源池总数
     */
    private int resourcePoolNum;

}

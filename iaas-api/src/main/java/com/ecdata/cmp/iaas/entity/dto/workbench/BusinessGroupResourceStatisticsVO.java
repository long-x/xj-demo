package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

/**
 * 业务组资源概况
 */
@Data
public class BusinessGroupResourceStatisticsVO {
    private Long key;//业务组id
    private String name;//业务组名称
    private String isApp;//是否是app
    private int resourceNum;//资源池数量
    private int cpu;//CPU配额总数
    private int memory;//内存配额总量（GB）
    private int virtual;//虚拟机容量总数
    private int virtualUse;//虚拟机已分配容量

    //树状形式组装
    private List<BusinessGroupResourceStatisticsVO> children;
}

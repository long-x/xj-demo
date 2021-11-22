package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceCapacityVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupUserVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostApplyCountVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserBusinessGroupResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.VirtualMachineCapacityVO;

import java.util.List;

public interface WorkbenchService {

    /**
     * 业务组云主机分布统计old
     *
     * @return
     */
    HostDistributionPieVO hostDistributionPieDataMap(List<Long> userBusinessGroup);

    /**
     * 业务组云主机分布统计new
     *
     * @return
     */
    HostDistributionPieVO hostDistributionPieDataMapNew(List<Long> userBusinessGroup);

    /**
     * 业务组成员数量
     *
     * @return
     */
    BusinessGroupUserVO queryBusinessGroupUserCount(List<Long> userBusinessGroup);

    /**
     * 业务组云主机申请趋势
     *
     * @return
     */
    List<HostApplyCountVO> queryBusinessApplyCount(List<Long> userBusinessGroup);

    /**
     * 业务组资源概况
     *
     * @return
     */
    List<BusinessGroupResourceStatisticsVO> queryBusinessGroupResourceStatistics(List<Long> userBusinessGroup);

    /**
     * 业务组人员资源分布old
     *
     * @return
     */
    List<UserBusinessGroupResourceVO> queryUserBusinessGroupResourceStatistics(List<Long> userBusinessGroup);

    /**
     * 应用资源分布new
     *
     * @return
     */
    List<UserBusinessGroupResourceVO> queryUserBusinessGroupResourceStatisticsNew(List<Long> userBusinessGroup);

    /**
     * 虚拟化服务器容量统计
     *
     * @return
     */
    List<VirtualMachineCapacityVO> queryVirtualMachineCapacity(List<Long> userBusinessGroup);

    /**
     * 业务资源容量占用最大TOP5 前五
     *
     * @return
     */
    BusinessGroupResourceCapacityVO queryBusinessGroupResourceCapacity(List<Long> userBusinessGroup);
}

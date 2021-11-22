package com.ecdata.cmp.iaas.mapper;

import com.ecdata.cmp.iaas.entity.dto.workbench.APPResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupUserDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostApplyCountDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieDataNewVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.VirtualMachineCapacityVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkbenchMapper {

    /**
     * 业务组云主机分布统计v1 old
     *
     * @return
     */
    List<HostDistributionPieDataVO> queryHostDistribution(List<Long> list);

    /**
     * 业务组云主机分布统计v2 new
     *
     * @return
     */
    List<HostDistributionPieDataNewVO> queryHostDistributionNew(List<Long> list);

    /**
     * 业务组成员数量
     *
     * @return
     */
    List<BusinessGroupUserDataVO> queryBusinessGroupUserCount(List<Long> list);

    /**
     * 业务组云主机申请趋势
     *
     * @return
     */
    List<HostApplyCountDataVO> queryBusinessApplyCount(List<Long> list);

    /**
     * 业务组资源概况
     *
     * @return
     */
    List<BusinessGroupStatisticsVO> queryBusinessGroupResourceStatistics(List<Long> list);

    /**
     * 业务组人员资源分布
     *
     * @return
     */
    List<UserResourceVO> queryUserBusinessGroupResourceStatistics(List<Long> list);

    /**
     * 应用资源分布new
     *
     * @return
     */
    List<APPResourceVO> queryUserBusinessGroupResourceStatisticsNew(List<Long> list);

    /**
     * 虚拟化服务器容量统计
     *
     * @return
     */
    List<VirtualMachineCapacityVO> queryVirtualMachineCapacity(List<Long> list);

}

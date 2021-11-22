package com.ecdata.cmp.iaas.mapper.report;

import com.ecdata.cmp.iaas.entity.dto.distribution.CpuAndCountVO;
import com.ecdata.cmp.iaas.entity.dto.distribution.DeptCountVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployBmVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployVmVO;
import com.ecdata.cmp.iaas.entity.dto.report.ConfigInfo;
import com.ecdata.cmp.iaas.entity.dto.report.IaasApplyReport;
import com.ecdata.cmp.iaas.entity.dto.resource.ResourceResultSafetyVO;
import com.ecdata.cmp.iaas.entity.dto.resource.ResourceResultVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.DistributionVMVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.InTransitBMVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.InTransitCMDVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/5/11 11:13
 */
@Mapper
@Repository
public interface IaasApplyReportMapper {
    List<IaasApplyReport> cloudResourceStatistics(@Param("businessName") String businessName);

    List<ConfigInfo> calculationResourceStatistics();

    List<ConfigInfo> securityResourceUsageReport();

    List<IaasApplyReport> cloudResourceAssignedStatistics(@Param("map") Map<String, Object> map);

    String queryParentName(@Param("id")Long id);

    List<Map<String,Object>> queryTowDep();

    List<Map<String,Object>> queryArea();

    ResourceResultVO resourceTrackingStatement1(@Param("map") Map<String, String> map);

    ResourceResultVO resourceTrackingStatement2(@Param("map") Map<String, String> map);

    List<ResourceResultVO> resourceTrackingStatement3(@Param("map") Map<String, String> map);

    ResourceResultSafetyVO resourceTrackingStatement4(@Param("type") String type,@Param("parentId") String parentId,@Param("time") String time);


    List<DeptCountVO> getDeptCount(@Param("projectId") String projectId);

    CpuAndCountVO getVmCpuAndCount(@Param("projectId") String projectId,@Param("azonId") String azonId);

    CpuAndCountVO getVmDisk(@Param("projectId") String projectId,@Param("azonId") String azonId);

    List<CpuAndCountVO> getBareMetalCount(@Param("projectId") String projectId,@Param("azonId") String azonId);

    //资源统计
    List<InTransitCMDVO> getInTransitCMD(@Param("map") Map<String, String> map);

    List<InTransitBMVO> getInTransitBM(@Param("map") Map<String, String> map);

    List<DistributionVMVO> getDistributionVM(@Param("map") Map<String, String> map);

    List<InTransitBMVO> getDistributionBM(@Param("map") Map<String, String> map);


    EmployVmVO getEmployVmDistribution(@Param("type") String type,@Param("remark")String remark);

    EmployVmVO getEmployInTransitVm(@Param("type") String type,@Param("remark")String remark);

    EmployVmVO getEmployVmBmSum(@Param("type") String type,@Param("remark")String remark);

    EmployBmVO getEmployBmDistribution(@Param("type") String type,@Param("remark")String remark);

    EmployBmVO getEmployInTransitBM(@Param("type") String type,@Param("remark")String remark);


}

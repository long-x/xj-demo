package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.iaas.entity.dto.employ.EmployVO;
import com.ecdata.cmp.iaas.entity.dto.report.CalculationResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceAssignedStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.StatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/5/11 14:00
 */
public interface IaasApplyReportService {

    /**
     * 云平台资源统计报表
     *
     * @param type
     * @param businessName
     * @return
     */
    List<CloudResourceStatisticsVO> cloudResourceStatistics(String type, String businessName);

    /**
     * 云平台资源分配情况
     *
     * @param parm
     * @return
     */
    List<CloudResourceAssignedStatisticsVO> cloudResourceAssignedStatistics(Map<String, Object> parm);

    /**
     * 计算资源统计表
     *
     * @param parm
     * @return
     */
    List<CalculationResourceStatisticsVO> calculationResourceStatistics(Map<String, Object> parm);

    /**
     * 安全资源使用报表
     *
     * @param parm
     * @return
     */
    List<CalculationResourceStatisticsVO> securityResourceUsageReport(Map<String, Object> parm);

    List<Map<String,Object>> queryTowDep();

    List<Map<String,Object>> queryArea();


    /**
     * 资源使用跟踪-1
     * @param map
     * @return
     */
    List<Map<String, String>> resourceTrackingStatement1(@Param("map") Map<String, String> map);


    /**
     * 云平台资源分配情况
     * @param
     */
    List<Map<String,String>> cloudResourceAssignedStatistics2();


    /**
     * 云平台资源统计报表(新)
     * @return
     */
    List<StatisticsVO> cloudResourceStatisticsNew(Map<String, String> parm);


    /**
     * 计算资源统计表(新)
     * @return
     */
    List<EmployVO> calculationResourceStatisticsNew();


}

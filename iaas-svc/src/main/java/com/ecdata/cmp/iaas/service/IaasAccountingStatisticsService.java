package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasAccountingStatistics;
import com.ecdata.cmp.iaas.entity.dto.ChargingVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingStatisticsExcelVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/15 10:50
 * @modified By：
 */
public interface IaasAccountingStatisticsService extends IService<IaasAccountingStatistics> {


    IPage<IaasAccountingStatisticsVO> qrStatisticsList(Page<IaasAccountingStatisticsVO> page, @Param("map") Map<String, Object> map);

    List<IaasAccountingStatisticsExcelVO> export(@Param("map") Map<String, Object> map);

    List<ChargingVO> qrCharging();

    boolean qrInfo();
}

package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasAccountingStatistics;
import com.ecdata.cmp.iaas.entity.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 10:56
 * @modified By：
 */
@Mapper
@Repository
public interface IaasAccountingStatisticsMapper extends BaseMapper<IaasAccountingStatistics> {

    IPage<IaasAccountingStatisticsVO> qrStatisticsList(Page<IaasAccountingStatisticsVO> page, @Param("map") Map<String, Object> map);

    List<IaasAccountingStatisticsExcelVO> qrStatisticsExcelList(@Param("map") Map<String, Object> map);


    List<ChargingVO> qrCharging();

    IaasAccountingRulesVO qrInfo();


    List<BMVO>qrBaremetal();
}

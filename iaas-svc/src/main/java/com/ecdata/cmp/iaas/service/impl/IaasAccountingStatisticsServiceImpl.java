package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasAccountingStatistics;
import com.ecdata.cmp.iaas.entity.dto.*;
import com.ecdata.cmp.iaas.mapper.IaasAccountingStatisticsMapper;
import com.ecdata.cmp.iaas.service.IaasAccountingStatisticsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 10:55
 * @modified By：
 */
@Service
public class IaasAccountingStatisticsServiceImpl extends ServiceImpl<IaasAccountingStatisticsMapper, IaasAccountingStatistics> implements IaasAccountingStatisticsService{


    @Override
    public IPage<IaasAccountingStatisticsVO> qrStatisticsList(Page<IaasAccountingStatisticsVO> page, Map<String, Object> map) {
        return baseMapper.qrStatisticsList(page,map);
    }


    @Override
    public List<IaasAccountingStatisticsExcelVO> export(Map<String, Object> map) {
        return baseMapper.qrStatisticsExcelList(map);
    }

    @Override
    public List<ChargingVO> qrCharging() {
        return baseMapper.qrCharging();
    }


    /**
     * 计量计费计划任务
     * @return
     */
    @Override
    public boolean qrInfo() {
        //获取按照业务组分类的信息
        List<ChargingVO> chargingVOS = baseMapper.qrCharging();
        //获取当前计量计费信息
        IaasAccountingRulesVO rulesVO = baseMapper.qrInfo();
        //裸金属信息
        List<BMVO> bmvos = baseMapper.qrBaremetal();
        if(rulesVO!=null){
            //合并结果集
            List<IaasAccountingStatistics> bean = new ArrayList<>();
            for (ChargingVO vo:chargingVOS) {
                IaasAccountingStatistics statistics = new IaasAccountingStatistics();
                statistics.setId(SnowFlakeIdGenerator.getInstance().nextId());
                statistics.setOrgId(vo.getGroupId());
                statistics.setOrgName(vo.getBusinessGroupName());
                statistics.setEffectiveDate(new Date());
                statistics.setStatus(0);
                statistics.setCpuCount(vo.getVcpuTotal());
                statistics.setCpuUnitPrice(rulesVO.getCpuUnitPrice());
                statistics.setMemoryCount(vo.getMemoryTotal());
                statistics.setMemoryUnitPrice(rulesVO.getMemoryUnitPrice());
                statistics.setDiskCount(vo.getDiskTotal());
                statistics.setDiskUnitPrice(rulesVO.getDiskUnitPrice());
                for (int j =0;j<bmvos.size();j++){
                    BMVO bmvo = bmvos.get(j);
                    if(vo.getGroupId().equals(bmvo.getBusinessGroupId())){
                        if("2288".equals(bmvo.getDetailName())){
                            statistics.setBmsType1Count(bmvo.getCount());
                            statistics.setBmsType1UnitPrice(rulesVO.getBmsType1UnitPrice());
                        }else if("2248".equals(bmvo.getDetailName())){
                            statistics.setBmsType2Count(bmvo.getCount());
                            statistics.setBmsType2UnitPrice(rulesVO.getBmsType2UnitPrice());
                        }
                    }
                }

                bean.add(statistics);
            }
            //插入数据库
            super.saveBatch(bean,bean.size());
            return true;
        }
        return false;
    }



}

package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasAccountingRules;
import com.ecdata.cmp.iaas.entity.IaasAccountingStatistics;
import com.ecdata.cmp.iaas.entity.dto.ChargingVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingRulesVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingStatisticsVO;
import com.ecdata.cmp.iaas.mapper.IaasAccountingRulesMapper;
import com.ecdata.cmp.iaas.service.IaasAccountingRulesService;
import com.ecdata.cmp.iaas.service.IaasAccountingStatisticsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
public class IaasAccountingRulesServiceImpl  extends ServiceImpl<IaasAccountingRulesMapper, IaasAccountingRules> implements IaasAccountingRulesService{
    @Override
    public int save(IaasAccountingRulesVO vo) {
        IaasAccountingRules iaasAccountingRules= new IaasAccountingRules();
        BeanUtils.copyProperties(vo,iaasAccountingRules);
        //确定时间段只有一条记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer exist = baseMapper.isExist(sdf.format(iaasAccountingRules.getEffectiveDate()), sdf.format(iaasAccountingRules.getExpirationDate()));
        if(exist<=0){
            //再次判断一下是否有生效的数据,只允许有一条生效的数据
            if(baseMapper.isTakeEffect()>0){
                iaasAccountingRules.setStatus(1);
            }
            iaasAccountingRules.setId(SnowFlakeIdGenerator.getInstance().nextId());
            iaasAccountingRules.setCreateTime(new Date());
            iaasAccountingRules.setUpdateTime(new Date());
            iaasAccountingRules.setCpuUnitPrice(iaasAccountingRules.getCpuUnitPrice()*100);
            iaasAccountingRules.setMemoryUnitPrice(iaasAccountingRules.getMemoryUnitPrice()*100);
            iaasAccountingRules.setDiskUnitPrice(iaasAccountingRules.getDiskUnitPrice()*100);
            iaasAccountingRules.setBmsType1UnitPrice(iaasAccountingRules.getBmsType1UnitPrice()*100);
            iaasAccountingRules.setBmsType2UnitPrice(iaasAccountingRules.getBmsType2UnitPrice()*100);
            iaasAccountingRules.setUpdateUser(Sign.getUserId());
            return  baseMapper.insert(iaasAccountingRules);
        }

        return 0;



    }


    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id,updateUser);
    }

    @Override
    public IPage<IaasAccountingRulesVO> qrList(Page<IaasAccountingRulesVO> page, Map<String, Object> map) {
        return baseMapper.qrList(page,map);
    }

    @Override
    public Integer isTodayExits(String today) {
        return baseMapper.isTodayExits(today);
    }

    //计划任务
    @Override
    public void updateTakeEffect(String today) {
        baseMapper.resetTakeEffect();
        baseMapper.updateTakeEffect(today);

    }
}

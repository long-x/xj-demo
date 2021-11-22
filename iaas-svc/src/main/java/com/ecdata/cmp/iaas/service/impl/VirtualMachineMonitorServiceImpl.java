package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineMonitor;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMonitorMapper;
import com.ecdata.cmp.iaas.service.IVirtualMachineMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: IVirtualMachineMonitorService
 * @Author: shig
 * @description: 虚拟机监控实现类
 * @Date: 2019/11/25 4:49 下午
 */
@Slf4j
@Service
public class VirtualMachineMonitorServiceImpl extends ServiceImpl<IaasVirtualMachineMonitorMapper, IaasVirtualMachineMonitor> implements IVirtualMachineMonitorService {
    @Override
    public List<IaasVirtualMachineMonitorVO> getClusterRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO) {
        return baseMapper.getClusterRateByVMMV(iaasVirtualMachineMonitorVO);
    }

    @Override
    public IPage<IaasVirtualMachineMonitorVO> getClusterRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map) {
        return baseMapper.getClusterRateByVMMVPage(page, map);
    }

    @Override
    public List<IaasVirtualMachineMonitorVO> getHostRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO) {
        return baseMapper.getHostRateByVMMV(iaasVirtualMachineMonitorVO);
    }

    @Override
    public IPage<IaasVirtualMachineMonitorVO> getHostRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map) {
        return baseMapper.getHostRateByVMMVPage(page, map);
    }

    @Override
    public List<IaasVirtualMachineMonitor> queryVMMVoInfoByType(Long type) {
        return baseMapper.queryVMMVoInfoByType(type);
    }

}
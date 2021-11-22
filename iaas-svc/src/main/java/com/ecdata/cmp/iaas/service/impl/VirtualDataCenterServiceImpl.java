package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.mapper.IaasVirtualDataCenterMapper;
import com.ecdata.cmp.iaas.service.IVirtualDataCenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: IVirtualDataCenterService
 * @Author: shig
 * @description:虚拟数据中心表(vdc)
 * @Date: 2019/12/13 5:11 下午
 */
@Slf4j
@Service
public class VirtualDataCenterServiceImpl extends ServiceImpl<IaasVirtualDataCenterMapper, IaasVirtualDataCenter> implements IVirtualDataCenterService {

    @Override
    public List<IaasVirtualDataCenter> getVdcNameByProviderId(Long providerId) {
        return baseMapper.getVdcNameByProviderId(providerId);
    }

    @Override
    public IaasVirtualDataCenterVO selectVdcById(String id) {
        return baseMapper.selectVdcById(id);
    }

}
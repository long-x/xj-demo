package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasAreaSum;
import com.ecdata.cmp.iaas.mapper.IaasAreaSumMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.service.IaasAreaSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hhj
 * @date 2020/5/26
 */
@Service
public class IaasAreaSumServiceImpl extends ServiceImpl<IaasAreaSumMapper, IaasAreaSum> implements IaasAreaSumService {

    @Autowired
    private IaasAreaSumMapper iaasAreaSumMapper;

    @Override
    public int getByServerNameAndAreaName(String serverName, String areaName) {

        return iaasAreaSumMapper.queryServerNameAndAreaName(serverName, areaName);
    }
}

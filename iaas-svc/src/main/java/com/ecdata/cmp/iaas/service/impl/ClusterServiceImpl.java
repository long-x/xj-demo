package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasCluster;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.service.IClusterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: IClusterServiceImpl
 * @Author: shig
 * @description: 集群实现类
 * @Date: 2019/11/19 10:30 上午
 */
@Slf4j
@Service
public class ClusterServiceImpl extends ServiceImpl<IaasClusterMapper, IaasCluster> implements IClusterService {

    @Override
    public void updateCluster(Long id, Long createUser) {
        baseMapper.updateCluster(id, createUser);
    }

    @Override
    public IPage<IaasClusterVo> queryClusterPage(Page<IaasClusterVo> page, String keyword) {
        return baseMapper.queryClusterPage(page, keyword);
    }

    @Override
    public List<IaasCluster> getClusterNameByProviderId(IaasClusterVo iaasClusterVo) {
        return baseMapper.getClusterNameByProviderId(iaasClusterVo);
    }

    @Override
    public List<IaasClusterVo> getInfoByClusterVO(IaasClusterVo iaasClusterVo) {
        return baseMapper.getInfoByClusterVO(iaasClusterVo);
    }

    @Override
    public IPage<IaasClusterVo> queryClusterVoPage(Page<IaasClusterVo> page, Map map) {
        return baseMapper.queryClusterVoPage(page, map);
    }
}
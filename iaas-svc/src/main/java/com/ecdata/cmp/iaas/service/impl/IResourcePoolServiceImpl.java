package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasResourcePool;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.mapper.IaasResourcePoolMapper;
import com.ecdata.cmp.iaas.service.IResourcePoolService;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: IResourcePoolServiceImpl
 * @Author: shig
 * @description: 资源池 实现类
 * @Date: 2019/11/18 4:35 下午
 */
@Slf4j
@Service
public class IResourcePoolServiceImpl extends ServiceImpl<IaasResourcePoolMapper, IaasResourcePool> implements IResourcePoolService {

    @Override
    public void updateResourcePool(Long id, Long createUser) {
        baseMapper.updateResourcePool(id, createUser);
    }

    @Override
    public IPage<ResourcePoolVO> queryResourcePoolPage(Page<ResourcePoolVO> page, Map<String, Object> params) {
        return baseMapper.queryResourcePoolPage(page, params);
    }

    @Override
    public IaasClusterVo getResourceSumInfoById(IaasClusterVo clusterVo) {
        return baseMapper.getResourceSumInfoById(clusterVo);
    }

    @Override
    public List<ResourcePoolVO> getDatastoreInfoByClusterId(ResourcePoolVO resourcePoolVO) {
        return baseMapper.getDatastoreInfoByClusterId(resourcePoolVO);
    }

    @Override
    public List<SysBusinessGroupVO> getBusinessGroupName(String businessGroupName) {
        return baseMapper.getBusinessGroupName(businessGroupName);
    }

    @Override
    public void removeBusinessGroupResourcePool(Long poolId) {
        baseMapper.removeBusinessGroupResourcePool(poolId);
    }

    @Override
    public void addBusinessGroupResourcePool(SysBusinessGroupResourcePoolVO businessGroupResourcePoolVO) {
        baseMapper.addBusinessGroupResourcePool(businessGroupResourcePoolVO);
    }

    @Override
    public List<SysBusinessGroupResourcePoolVO> getBusinessGroupNameByPoolId(Long poolId) {
        return baseMapper.getBusinessGroupNameByPoolId(poolId);
    }

    @Override
    public List<ResourcePoolVO> getPoolByGroupId(Long id) {
        return baseMapper.getPoolByGroupId(id);
    }


}
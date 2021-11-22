package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasResourcePoolDatastore;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
import com.ecdata.cmp.iaas.mapper.IaasResourcePoolDatastoreMapper;
import com.ecdata.cmp.iaas.service.IResourcePoolDatastoreService;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: IDatastoreServiceImpl
 * @Author: shig
 * @description: 资源池 实现类
 * @Date: 2019/11/18 4:35 下午
 */
@Slf4j
@Service
public class IResourcePoolDatastoreServiceImpl extends ServiceImpl<IaasResourcePoolDatastoreMapper, IaasResourcePoolDatastore> implements IResourcePoolDatastoreService {

    @Override
    public void updateResourcePoolDatastore(Long id, Long createUser) {
        baseMapper.updateResourcePoolDatastore(id, createUser);
    }

    @Override
    public IPage<ResourcePoolDatastoreVO> queryResourcePoolDatastorePage(Page<ResourcePoolDatastoreVO> page, Map<String, Object> params) {
        return baseMapper.queryResourcePoolDatastorePage(page, params);
    }

    @Override
    public void removeByPoolId(Long poolId, Long createUser) {
        baseMapper.removeByPoolId(poolId, createUser);
    }

}
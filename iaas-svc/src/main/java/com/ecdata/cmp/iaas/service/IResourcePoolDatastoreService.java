package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasResourcePoolDatastore;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;

import java.util.Map;

/**
 * @title: IResourcePoolDatastoreService
 * @Author: shig
 * @description: 资源池存储 业务层
 * @Date: 2019/11/18 4:03 下午
 */
public interface IResourcePoolDatastoreService extends IService<IaasResourcePoolDatastore> {
    /**
     * 修改更新记录
     *
     * @param id         id
     * @param createUser 创建人id
     */
    void updateResourcePoolDatastore(Long id, Long createUser);

    /**
     * 分页获取信息
     *
     * @param page   分页
     * @param params 关键字
     * @return IPage<ResourcePoolDatastoreVO>
     */
    IPage<ResourcePoolDatastoreVO> queryResourcePoolDatastorePage(Page<ResourcePoolDatastoreVO> page, Map<String, Object> params);

    void removeByPoolId(Long poolId, Long createUser);
}
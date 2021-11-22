package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasResourcePool;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;

import java.util.List;
import java.util.Map;

/**
 * @title: IResourcePoolService
 * @Author: shig
 * @description: 资源池 业务层
 * @Date: 2019/11/18 4:03 下午
 */
public interface IResourcePoolService extends IService<IaasResourcePool> {
    /**
     * 修改更新记录
     *
     * @param id         供应商id
     * @param createUser 创建人id
     */
    void updateResourcePool(Long id, Long createUser);

    /**
     * 分页获取连接池信息
     *
     * @param page   分页
     * @param params
     * @return IPage<UserVO>
     */
    IPage<ResourcePoolVO> queryResourcePoolPage(Page<ResourcePoolVO> page, Map<String, Object> params);

    IaasClusterVo getResourceSumInfoById(IaasClusterVo iaasClusterVo);

    List<ResourcePoolVO> getDatastoreInfoByClusterId(ResourcePoolVO resourcePoolVO);

    List<SysBusinessGroupVO> getBusinessGroupName(String businessGroupName);

    void removeBusinessGroupResourcePool(Long poolId);

    void addBusinessGroupResourcePool(SysBusinessGroupResourcePoolVO businessGroupResourcePoolVO);

    List<SysBusinessGroupResourcePoolVO> getBusinessGroupNameByPoolId(Long poolId);

    List<ResourcePoolVO> getPoolByGroupId(Long id);

}
package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.dto.ContainerImageResourceVO;
import com.ecdata.cmp.iaas.entity.dto.HuaWeiContainerImageResourceVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ProviderResponse;

import java.util.List;
import java.util.Map;

/**
 * @title: iaasProvider interface
 * @Author: shig
 * @description: 供应商接口
 * @Date: 2019/11/12 4:34 下午
 */
public interface IIaasProviderService extends IService<IaasProvider> {

    IaasProvider getInfoByProvider(IaasProvider iaasProvider);

    /**
     * 修改更新记录
     *
     * @param id         供应商id
     * @param createUser 创建人id
     */
    void updateIaasProvider(Long id, Long createUser);

    /**
     * 分页获取供应商信息
     *
     * @param page    分页
     * @param keyword 关键字
     * @return IPage<UserVO>
     */
    IPage<IaasProviderVO> queryIaasProviderPage(Page<IaasProviderVO> page, String keyword, String type);

    /**
     * 查询供应商区域，集群，主机等信息
     *
     * @param iaasProvider
     */
    ContainerImageResourceVO queryIaasProviderInfo(IaasProviderVO iaasProvider);

    /**
     * 统计华为资源信息
     *
     * @param providerId
     * @return
     */
    HuaWeiContainerImageResourceVO queryHuaWeiResourceInfo(Long providerId);

    /**
     * 查询华为应商区域，集群，vdc信息
     *
     * @param iaasProvider
     */
    ProviderResponse queryIaasProviderHuaWeiInfo(IaasProviderVO iaasProvider);

    /**
     * 供应商名称去重
     *
     * @param providerName
     * @return
     */
    List<IaasProvider> disProviderName(String providerName);

    List<IaasProviderVO> providerlistByMap(Map map);

    /**
     * 根据供应商id获取vdc信息
     *
     * @param providerId
     * @return
     */
    List<IaasVirtualDataCenterVO> queryIaasVDCInfoByProviderId(Long providerId);


    /**
     * 供应商集联
     *
     * @return
     */
    List<IaasProviderVO> getCloudPlatformEntrance();

}
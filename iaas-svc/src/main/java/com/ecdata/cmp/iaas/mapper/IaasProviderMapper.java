package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ProviderResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.cascade.ProviderCascade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IaasProviderMapper extends BaseMapper<IaasProvider> {

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
     * @return IPage<IaasProviderVO>
     */
    IPage<IaasProviderVO> queryIaasProviderPage(Page<IaasProviderVO> page, @Param("keyword") String keyword, @Param("type") String type);


    ProviderResponse queryDataCenter(IaasProviderVO iaasProvider);


    /**
     * 供应商名称去重
     *
     * @param providerName
     * @return
     */
    List<IaasProvider> disProviderName(@Param("providerName") String providerName);

    /**
     * 根据供应id获取供应商信息
     *
     * @param id
     * @return
     */
    IaasProviderVO queryIaasProviderVOById(@Param("id") Long id);

    List<IaasProviderVO> providerlistByMap(@Param("map") Map map);

    List<IaasProviderVO> getCloudPlatformEntrance();

    /**
     * 根据集群id获取供应商id
     *
     * @param clusterId
     * @return
     */
    Long queryProviderIdByClusterId(Long clusterId);

    @Select("select id from iaas_provider")
    Long queryProviderId();

    IaasProviderVO queryProviderByAreaId(Long id);

    /**
     * 根据供应商id查出级联信息
     *
     * @param providerId
     * @return
     */
    ProviderCascade queryProviderCascadeByProvider(@Param("providerId") Long providerId);
}
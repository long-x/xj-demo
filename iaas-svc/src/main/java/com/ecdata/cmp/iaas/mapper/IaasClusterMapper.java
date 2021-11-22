package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasCluster;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasClusterMapper extends BaseMapper<IaasCluster> {
    /**
     * 修改更新记录
     *
     * @param id         id
     * @param createUser 创建人id
     */
    void updateCluster(Long id, Long createUser);

    /**
     * 分页获取信息
     *
     * @param page    分页
     * @param keyword 关键字
     * @return IPage<IaasClusterVo>
     */
    IPage<IaasClusterVo> queryClusterPage(Page<IaasClusterVo> page, String keyword);

    /**
     * getClusterNameByProviderId
     *
     * @param iaasClusterVo
     * @return
     */
    List<IaasCluster> getClusterNameByProviderId(IaasClusterVo iaasClusterVo);

    List<IaasClusterVo> getInfoByClusterVO(IaasClusterVo iaasClusterVo);

    IPage<IaasClusterVo> queryClusterVoPage(Page<IaasClusterVo> page, @Param("map") Map map);

    /**
     * 根据集群id获取区域id
     *
     * @param clusterId
     * @return
     */
    Long queryAreaIdByClusterId(Long clusterId);

    /**
     * 根据key查询出信息
     *
     * @param clusterKey
     * @return
     */
    IaasCluster queryIaasClusterByKey(String clusterKey);

    /**
     *根据区域ids获取集群ids
     * @param list
     * @return
     */
    List<Long> queryClusterByAreaIds(List<Long> list);
}

package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasCluster;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @title: IClusterService
 * @Author: shig
 * @description: 集群 接口
 * @Date: 2019/11/19 10:29 上午
 */
public interface IClusterService extends IService<IaasCluster> {
    /**
     * 修改更新记录
     *
     * @param id         集群id
     * @param createUser 创建人id
     */
    void updateCluster(Long id, Long createUser);

    /**
     * 分页获取集群信息
     *
     * @param page    分页
     * @param keyword 关键字
     * @return IPage<UserVO>
     */
    IPage<IaasClusterVo> queryClusterPage(Page<IaasClusterVo> page, String keyword);


    /**
     * getClusterNameByProviderId
     *
     * @param iaasClusterVo
     * @return
     */
    List<IaasCluster> getClusterNameByProviderId(IaasClusterVo iaasClusterVo);

    /**
     * @param iaasClusterVo
     * @return
     */
    List<IaasClusterVo> getInfoByClusterVO(IaasClusterVo iaasClusterVo);

    IPage<IaasClusterVo> queryClusterVoPage(Page<IaasClusterVo> page, Map map);

}
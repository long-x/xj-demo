package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasHost;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @title: host
 * @Author: shig
 * @description: 主机 服务层
 * @Date: 2019/11/19 8:07 下午
 */
public interface IHostService extends IService<IaasHost> {

    /**
     * 查询总和：物理CPU核心总数量 cpuTotals、物理内存数量 memoryTotals
     *
     * @param clusterId
     * @return
     */
    IaasHost getTotalRatio(Long clusterId);

    /**
     * 根据集群id查询 主机内存和
     *
     * @param clusterId
     * @return
     */
    String getSumByClusterId(@Param("clusterId") Long clusterId);


    /**
     * 条件查询主机使用率情况
     *
     * @param map
     * @return
     */
    IPage<IaasHostVO> qrIssHostList(Page<IaasHostVO> page, @Param("map") Map<String, Object> map);


    /**
     * 查询单个主机使用率情况
     */
    IaasHostVO qrIssHostInfo(@Param("id") String id);


    List<IaasHostVO> getInfoByHostVO(IaasHostVO iaasHostVO);

    IPage<IaasHostVO> queryHostVoPage(Page<IaasHostVO> page, Map map);

    IPage<IaasHostVO> queryHostInfoPage(Page<IaasHostVO> page, Map map);

}
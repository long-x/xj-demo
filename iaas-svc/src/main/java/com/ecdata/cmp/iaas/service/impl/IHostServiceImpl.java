package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasHost;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import com.ecdata.cmp.iaas.mapper.IaasHostMapper;
import com.ecdata.cmp.iaas.service.IHostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: host impl
 * @Author: shig
 * @description: 主机 实现层
 * @Date: 2019/11/19 8:08 下午
 */

@Slf4j
@Service
public class IHostServiceImpl extends ServiceImpl<IaasHostMapper, IaasHost> implements IHostService {

    /**
     * 查询总和：物理CPU核心总数量 cpuTotals、物理内存数量 memoryTotals
     *
     * @param clusterId
     * @return
     */
    @Override
    public IaasHost getTotalRatio(Long clusterId) {
        return baseMapper.getTotalRatio(clusterId);
    }

    @Override
    public String getSumByClusterId(Long clusterId) {
        return baseMapper.getSumByClusterId(clusterId);
    }


    /**
     * 条件查询主机使用率情况
     *
     * @param map
     * @return
     */
    @Override
    public IPage<IaasHostVO> qrIssHostList(Page<IaasHostVO> page, Map<String, Object> map) {
        return baseMapper.qrIssHostList(page, map);
    }

    /**
     * 查询单个主机使用率情况
     */
    @Override
    public IaasHostVO qrIssHostInfo(String id) {
        return baseMapper.qrIssHostInfo(id);
    }

    @Override
    public List<IaasHostVO> getInfoByHostVO(IaasHostVO iaasHostVO) {
        return baseMapper.getInfoByHostVO(iaasHostVO);
    }

    @Override
    public IPage<IaasHostVO> queryHostVoPage(Page<IaasHostVO> page, @Param("map") Map map) {
        return baseMapper.queryHostVoPage(page, map);
    }

    @Override
    public IPage<IaasHostVO> queryHostInfoPage(Page<IaasHostVO> page, @Param("map") Map map) {
        return baseMapper.queryHostInfoPage(page, map);
    }


}
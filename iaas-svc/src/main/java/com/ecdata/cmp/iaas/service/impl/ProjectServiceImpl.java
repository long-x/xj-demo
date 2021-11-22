package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasProject;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.BppvVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.response.project.BusinessGroupCascade;
import com.ecdata.cmp.iaas.mapper.IaasProjectMapper;
import com.ecdata.cmp.iaas.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: IVirtualDataCenterService
 * @Author: shig
 * @description:虚拟数据中心表(vdc)
 * @Date: 2019/12/13 5:11 下午
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<IaasProjectMapper, IaasProject> implements IProjectService {

    @Override
    public List<IaasProject> getProjectNameByVdcId(Long vdcId) {
        return baseMapper.getProjectNameByVdcId(vdcId);
    }

    @Override
    public IaasProjectVO getByProjecAndVdcId(Long id) {
        return baseMapper.getByProjecAndVdcId(id);
    }

    @Override
    public IaasProjectVO queryIaasProject(IaasProjectVO projectVO) {
        return baseMapper.queryIaasProject(projectVO);
    }

    @Override
    public IaasProject queryIaasProjectById(Long projectId) {
        return baseMapper.queryIaasProjectById(projectId);
    }

    @Override
    public List<IaasVirtualDataCenterVO> queryVdcName(Long userId) {
        return baseMapper.queryVdcName(userId);
    }

    @Override
    public List<BusinessGroupCascade> queryProjectByGroupId(Long userId) {
        return baseMapper.queryProjectByGroupId(userId);
    }

    @Override
    public BppvVO queryAllByGroupId(Long groupId) {
        return baseMapper.queryAllByGroupId(groupId);
    }
}
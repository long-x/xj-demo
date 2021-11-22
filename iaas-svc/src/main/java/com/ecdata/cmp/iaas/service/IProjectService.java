package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasProject;
import com.ecdata.cmp.iaas.entity.dto.BppvVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.response.project.BusinessGroupCascade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: IProjectService interface
 * @Author: shig
 * @description:项目接口
 * @Date: 2019/12/13 4:34 下午
 */
public interface IProjectService extends IService<IaasProject> {

    /**
     * 根据vdc id查询
     *
     * @param vdcId
     * @return
     */
    List<IaasProject> getProjectNameByVdcId(Long vdcId);

    IaasProjectVO getByProjecAndVdcId(Long id);

    IaasProjectVO queryIaasProject(IaasProjectVO projectVO);

    IaasProject queryIaasProjectById(Long projectId);

    List<IaasVirtualDataCenterVO> queryVdcName(Long userId);

    List<BusinessGroupCascade> queryProjectByGroupId(Long userId);

    BppvVO queryAllByGroupId(@Param("groupId") Long groupId);
}
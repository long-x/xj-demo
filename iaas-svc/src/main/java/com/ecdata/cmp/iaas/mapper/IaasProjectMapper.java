package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasProject;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.BppvVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.response.project.BusinessGroupCascade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasProjectMapper extends BaseMapper<IaasProject> {
    IaasProjectVO queryIaasProject(IaasProjectVO projectVO);

    IaasProject queryIaasProjectByKey(String key);

    /**
     * 根据vdc_id查询
     *
     * @param vdcId
     * @return
     */
    List<IaasProject> getProjectNameByVdcId(@Param("vdcId") Long vdcId);

    IaasProjectVO getByProjecAndVdcId(@Param("id") Long id);

    IaasProject queryIaasProjectById(Long id);

    /**
     * 根据用户id查询vdc名称
     * @param userId
     * @return
     */
    List<IaasVirtualDataCenterVO> queryVdcName(@Param("userId") Long userId);



    /**
     * 根据userid查出级联信息
     *
     * @param userId
     * @return
     */
    List<BusinessGroupCascade> queryProjectByGroupId(@Param("userId") Long userId);


    /**
     * 根据业务组id查出关联的vdc 项目
     * @param groupId
     * @return
     */
    BppvVO queryAllByGroupId(@Param("groupId") Long groupId);

}

package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyConfigInfo;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyResource;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyServiceSecurityResourcesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IaasApplyResourceMapper extends BaseMapper<IaasApplyResource> {

    /**
     * 根据申请资源id查询
     *
     * @param applyId
     * @return
     */
    List<IaasApplyResourceVO> queryApplyResource(@Param("applyId") Long applyId);

    IPage<IaasApplyResourceVO> queryApplyResourcePage(Page<IaasApplyResourceVO> page, @Param("map") Map<String, Object> map);

    /**
     * 查询租期到期的申请资源
     *
     * @param date
     * @return
     */
    List<IaasApplyResourceVO> queryApplyResourceLeaseExpire(String date);

    List<CloudVmVO> queryCloudVm(Long id);

    IaasApplyResourceVO queryApplyResourceByPro(String processInstanceId);

    List<IaasApplyConfigInfoVO> queryApplyServiceSecurityResources(@Param("applyType")int applyType, @Param("sysBusinessGroupId")String sysBusinessGroupId);



}

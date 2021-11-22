package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasVirtualDataCenterMapper extends BaseMapper<IaasVirtualDataCenter> {
    IaasVirtualDataCenter queryIaasVirtualDataCenterByKey(String vdcKey);

    List<IaasVirtualDataCenterVO> queryIaasVirtualDataCentersByClusterIds(Long providerId);

    /**
     * 根据供应商id查询
     *
     * @param providerId
     * @return
     */
    List<IaasVirtualDataCenter> getVdcNameByProviderId(@Param("providerId") Long providerId);



    IaasVirtualDataCenterVO selectVdcById(@Param("id") String id);


    /**
     * 查询所有裸金属
     */
    List<BareMetalVO> queryBareMetalList();


}

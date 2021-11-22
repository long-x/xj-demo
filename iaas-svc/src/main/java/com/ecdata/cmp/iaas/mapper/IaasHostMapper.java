package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasHost;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IaasHostMapper extends BaseMapper<IaasHost> {

    IaasHost getTotalRatio(Long clusterId);

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

    IaasHost queryIaasHostByKey(String hostKey);
}
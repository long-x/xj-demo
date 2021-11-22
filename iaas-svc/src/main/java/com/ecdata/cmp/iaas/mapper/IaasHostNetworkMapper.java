package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasHostNetwork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasHostNetworkMapper extends BaseMapper<IaasHostNetwork> {
    IaasHostNetwork queryHostNetworkByHostIdAndNetworkId(@Param("hostId") long hostId, @Param("networkId") Long networkId);
}

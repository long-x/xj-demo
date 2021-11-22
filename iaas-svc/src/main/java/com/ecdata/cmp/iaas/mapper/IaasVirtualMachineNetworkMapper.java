package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineNetwork;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasVirtualMachineNetworkMapper extends BaseMapper<IaasVirtualMachineNetwork> {
}

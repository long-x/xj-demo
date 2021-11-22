package com.ecdata.cmp.iaas.mapper.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApply;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineNetwork;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IaasProcessApplyVirtualMachineNetworkMapper extends BaseMapper<IaasProcessApplyVirtualMachineNetwork> {
}

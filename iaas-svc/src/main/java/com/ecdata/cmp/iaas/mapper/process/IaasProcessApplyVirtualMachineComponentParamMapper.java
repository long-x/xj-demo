package com.ecdata.cmp.iaas.mapper.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IaasProcessApplyVirtualMachineComponentParamMapper extends BaseMapper<IaasProcessApplyVirtualMachineComponentParam> {
    void deleteByVmComponentId(Long vmComponentId);
}

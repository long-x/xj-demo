package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponentParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasTemplateVirtualMachineComponentParamMapper extends BaseMapper<IaasTemplateVirtualMachineComponentParam> {

    void deleteByVmComponentId(Long vmComponentId);
}

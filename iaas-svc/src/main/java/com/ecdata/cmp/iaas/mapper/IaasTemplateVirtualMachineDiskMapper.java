package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineDisk;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasTemplateVirtualMachineDiskMapper extends BaseMapper<IaasTemplateVirtualMachineDisk> {
}

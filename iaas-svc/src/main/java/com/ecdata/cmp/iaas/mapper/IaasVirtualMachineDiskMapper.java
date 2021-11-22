package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineDisk;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasVirtualMachineDiskMapper extends BaseMapper<IaasVirtualMachineDisk> {
    IaasVirtualMachineDisk queryVMDiskBy(String key);
}

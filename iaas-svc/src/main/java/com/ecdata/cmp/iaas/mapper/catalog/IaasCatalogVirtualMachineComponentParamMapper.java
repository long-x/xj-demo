package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IaasCatalogVirtualMachineComponentParamMapper extends BaseMapper<IaasCatalogVirtualMachineComponentParam> {
    void deleteByVmComponentId(Long vmComponentId);
}

package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IaasCatalogVirtualMachineComponentMapper extends BaseMapper<IaasCatalogVirtualMachineComponent> {
}

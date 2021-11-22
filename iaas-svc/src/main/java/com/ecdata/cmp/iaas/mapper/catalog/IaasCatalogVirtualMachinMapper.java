package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IaasCatalogVirtualMachinMapper extends BaseMapper<IaasCatalogVirtualMachin> {
}

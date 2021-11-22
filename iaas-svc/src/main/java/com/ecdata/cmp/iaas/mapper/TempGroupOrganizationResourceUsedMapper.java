package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.TempGroupOrganizationResourceUsed;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TempGroupOrganizationResourceUsedMapper extends BaseMapper<TempGroupOrganizationResourceUsed> {
}
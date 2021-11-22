package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyMemoryDisk;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasApplyMemoryDiskMapper extends BaseMapper<IaasApplyMemoryDisk> {
    List<Long> queryDiskByConfigId(Long configId);
}

package com.ecdata.cmp.iaas.mapper.apply;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyNetworkAsk;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasApplyNetworkAskMapper extends BaseMapper<IaasApplyNetworkAsk> {
    List<Long> queryNetworkByConfigId(Long configId);
}

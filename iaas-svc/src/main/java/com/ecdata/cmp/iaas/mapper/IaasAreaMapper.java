package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasAreaMapper extends BaseMapper<IaasArea> {

    IaasArea queryIaasAreaByKey(String key);

    List<Long> queryIaasAreaIdsByProviderId(Long providerId);

}

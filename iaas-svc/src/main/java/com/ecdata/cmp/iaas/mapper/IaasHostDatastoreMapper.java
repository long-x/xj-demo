package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasHostDatastore;
import com.ecdata.cmp.iaas.entity.dto.IaasHostDatastoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasHostDatastoreMapper extends BaseMapper<IaasHostDatastore> {

    List<IaasHostDatastoreVO> queryHostDatastoreVO(IaasHostDatastoreVO datastoreVO);

    IaasHostDatastore queryHostDatastoreByKey(String datastoreKey);
}

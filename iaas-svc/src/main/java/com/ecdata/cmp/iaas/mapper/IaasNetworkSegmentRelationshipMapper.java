package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasNetworkSegmentRelationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Mapper
@Repository
public interface IaasNetworkSegmentRelationshipMapper extends BaseMapper<IaasNetworkSegmentRelationship> {

}

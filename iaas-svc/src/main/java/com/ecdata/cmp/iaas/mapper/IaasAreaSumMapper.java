package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasAreaSum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author hhj
 * @since 2020-05-26
 */
@Mapper
@Repository
public interface IaasAreaSumMapper extends BaseMapper<IaasAreaSum> {

    Integer queryServerNameAndAreaName(@Param("serverName") String serverName, @Param("areaName") String areaName);

}

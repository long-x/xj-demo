package com.ecdata.cmp.apigateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 14:37
 * @modified By：
 */
@Mapper
@Repository
public interface LogValidityMapper extends BaseMapper<LogValidity> {

    Long isExist(@Param("userId") Long userId);

    Long getTime(LogValidity logValidity);

    int saveLogValidity(LogValidity logValidity);

    int updateLogValidity(LogValidity logValidity);
}

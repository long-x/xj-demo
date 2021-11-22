package com.ecdata.cmp.apigateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.apigateway.entity.SysLog;
import com.ecdata.cmp.apigateway.entity.response.SysLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {
    IPage<SysLogVO> queryLogPageByMap(Page page, @Param("map") Map map);

    Long querySysLogCount(@Param("map") Map map);
}
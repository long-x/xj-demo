package com.ecdata.cmp.apigateway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.apigateway.entity.SysLog;
import com.ecdata.cmp.apigateway.entity.response.SysLogVO;

import java.util.Map;

public interface ISysLogService extends IService<SysLog> {
    IPage<SysLogVO> queryLogPageByMap(Page<SysLogVO> page, Map map);

    Long querySysLogCount(Map map);

    int insert(SysLog sysLog);
}

package com.ecdata.cmp.apigateway.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.apigateway.entity.SysLog;
import com.ecdata.cmp.apigateway.entity.response.SysLogVO;
import com.ecdata.cmp.apigateway.mapper.SysLogMapper;
import com.ecdata.cmp.apigateway.service.ISysLogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @title: ISysLogService Impl
 * @Author: shig
 * @description: 实现类
 * @Date: 2019/11/27 11:09 上午
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public IPage<SysLogVO> queryLogPageByMap(Page<SysLogVO> page, Map map) {
        return baseMapper.queryLogPageByMap(page, map);
    }

    @Override
    public Long querySysLogCount(Map map){
        return baseMapper.querySysLogCount(map);
    }

    @Override
    public int insert(SysLog sysLog) {
        return baseMapper.insert(sysLog);
    }
}
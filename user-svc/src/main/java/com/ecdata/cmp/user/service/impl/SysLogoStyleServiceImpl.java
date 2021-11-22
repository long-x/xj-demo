package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.SysLogoStyle;
import com.ecdata.cmp.user.mapper.SysLogoStyleMapper;
import com.ecdata.cmp.user.service.ISysLogoStyleService;
import org.springframework.stereotype.Service;

/**
 * @title: ISysLogoStyleServiceImpl
 * @Author: shig
 * @description: 系统logo样式实现类
 * @Date: 2019/11/22 9:04 下午
 */
@Service
public class SysLogoStyleServiceImpl extends ServiceImpl<SysLogoStyleMapper, SysLogoStyle> implements ISysLogoStyleService {

    @Override
    public SysLogoStyle getLastTimeSysLogoStyle() {
        return baseMapper.getLastTimeSysLogoStyle();
    }
}
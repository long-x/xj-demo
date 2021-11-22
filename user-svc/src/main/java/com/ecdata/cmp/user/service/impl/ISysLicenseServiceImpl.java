package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.SysLicense;
import com.ecdata.cmp.user.mapper.SysLicenseMapper;
import com.ecdata.cmp.user.service.ISysLicenseService;
import org.springframework.stereotype.Service;

/**
 * @title: ISysLicenseServiceImpl
 * @Author: shig
 * @description: 许可证 实现类
 * @Date: 2019/11/21 11:19 下午
 */
@Service
public class ISysLicenseServiceImpl extends ServiceImpl<SysLicenseMapper, SysLicense> implements ISysLicenseService {

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public SysLicense getLastTimeSysLicense() {
        return baseMapper.getLastTimeSysLicense();
    }
}
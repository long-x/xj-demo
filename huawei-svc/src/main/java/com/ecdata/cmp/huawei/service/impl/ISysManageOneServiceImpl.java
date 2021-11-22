package com.ecdata.cmp.huawei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.huawei.dto.alarm.SysManageOne;
import com.ecdata.cmp.huawei.mapper.SysManageOneMapper;
import com.ecdata.cmp.huawei.service.ISysManageOneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ISysManageOneServiceImpl extends ServiceImpl<SysManageOneMapper, SysManageOne>
        implements ISysManageOneService {
    @Override
    public void updateOccurById(SysManageOne entity) {
        baseMapper.updateOccurTime(entity);
    }

    @Override
    public void saveSysManageOne(SysManageOne entity) {
        baseMapper.saveSysManageOne(entity);
    }

    @Override
    public SysManageOne selectById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int selectCountSysManageOne() {
        return baseMapper.selectCountSysManageOne();
    }

}

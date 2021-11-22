package com.ecdata.cmp.huawei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.huawei.dto.alarm.SysManageOne;


public interface ISysManageOneService extends IService<SysManageOne> {
    /**
     * 根据id更新执行时间
     */
    void updateOccurById(SysManageOne entity);
    /**
     * 保存执行时间
     */
    void saveSysManageOne(SysManageOne entity);
    /**
     * 根据id查询执行时间
     */
    SysManageOne selectById(Long id);
    /**
     * 查询数据库记录数量
     */
    int selectCountSysManageOne();

}

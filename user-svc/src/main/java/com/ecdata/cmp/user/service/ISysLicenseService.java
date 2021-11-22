package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.SysLicense;

public interface ISysLicenseService extends IService<SysLicense> {
    /**
     * 逻辑删除
     *
     * @param id
     * @param updateUser
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 获取最新一次时间
     *
     * @return
     */
    SysLicense getLastTimeSysLicense();
}

package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.Python;

/**
 * @author xuxinsheng
 * @since 2019-11-22
 */
public interface IPythonService extends IService<Python> {
    /**
     * /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);
}

package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.SysNotificationReceiver;

import java.util.List;
import java.util.Map;

public interface SysNotificationReceiverService extends IService<SysNotificationReceiver> {

    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);
    /**
     * @Description: 个人消息已读
     * @Date: 2019/11/26 20:37
     */
    void updateUserMessageStatus(Long userId);


    //批量删除  以,拼接
    public Integer batchRemove(String ids, Long updateUser);
}

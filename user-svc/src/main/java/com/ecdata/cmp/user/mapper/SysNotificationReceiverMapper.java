package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.SysNotificationReceiver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 13:43 2019/11/15
 **/
@Mapper
@Repository
public interface SysNotificationReceiverMapper extends BaseMapper<SysNotificationReceiver> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);


    void updateUserMessageStatus(Long userId);

}

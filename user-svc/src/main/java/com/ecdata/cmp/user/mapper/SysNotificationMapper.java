package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.entity.SysNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SysNotificationMapper extends BaseMapper<SysNotification> {

    @Select("Select distinct `csn` From sys_notification where `csn` is not null and`status`=1")
    List<Long> getAllCsn();


    @Update("UPDATE sys_notification SET status = #{status}, update_time = NOW(), update_user = #{userId}, operator = #{userId} " +
            " WHERE `type` = 2 AND `status` = 1 AND  is_deleted = 0 AND id =  #{id}")
    void updateOneNotifiStatus(@Param("id") Long id, @Param("userId") Long userId, @Param("status") int status);

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    List<SysNotificationVO> getUserNotification(@Param("map") Map<String, Object>  map);

    IPage<SysNotificationVO> getUserNotification(Page page, @Param("map") Map<String, Object>  map);


    void updateReceiverMessageStatus(@Param("id") Long id, @Param("userId") Long userId,
                                     @Param("remark") String remark);

    void updateReceiverRemark(@Param("csnList") List<Long> csnList, @Param("remark") String remark);

    List<Long> getUserHuaweiMessageId(@Param("userId") Long userId);

    SysNotification selectByReceivId(@Param("receiveId") Long receiveId);

    void updateNotifiStatus(@Param("userId") Long userId, @Param("status") int status,
                            @Param("useCsn") String useCsn, @Param("csnList") List<Long> csnList);

}

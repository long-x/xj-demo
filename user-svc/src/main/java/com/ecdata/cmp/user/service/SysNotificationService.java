package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.entity.SysNotification;

import java.util.List;
import java.util.Map;

public interface SysNotificationService extends IService<SysNotification> {

    List<SysNotificationVO> getUserNotification(Long userId, Integer status);

    /**
     * 分页查询业务组信息
     */
    IPage<SysNotificationVO> getUserNotificationPage(Page page, Map<String, Object>  map);

    /**
     * @Description: 个人消息已读
     * @Date: 2019/11/26 20:37
     */
    BaseResponse updateBatchUserMessageStatus(Long userId, String remark);

    boolean oneDealMessage(Long id, Long updateUser, String remark);

    boolean dealSangforNotify(Long id, Long updateUser, String remark);

    Integer datchBealMessage(String ids, Long updateUser, String remark);


    BaseResponse addNotificationToUser(SysNotificationRequest sysNotificationRequest);

    BaseResponse addNotifToUserByType(SysNotificationRequest notiRequest);

    List<Long> getAllCsn();

    List<Long> addNotifToBell(SysNotificationVO notifiVO);
}

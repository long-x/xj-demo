package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.dto.request.SysNotifyManageRequest;
import com.ecdata.cmp.user.entity.SysNotifyManage;
import com.ecdata.cmp.user.mapper.*;
import com.ecdata.cmp.user.service.SysNotificationService;
import com.ecdata.cmp.user.service.SysNotifyManageService;
import com.ecdata.cmp.user.utils.EmailUtil;
import com.ecdata.cmp.user.utils.QXTTextMsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 2020/3/1 22:07
 */
@Slf4j
@Service
public class SysNotifyManageServiceImpl extends ServiceImpl<SysNotifyManageMapper, SysNotifyManage> implements SysNotifyManageService {


    @Autowired
    SysNotificationService sysNotificationService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QXTTextMsgUtils qxtTextMsgUtils;

    @Value("${email.personal}")
    private String emailPersonal;

    @Value("${email.account}")
    private String emailAccount;//账户名:,此账户必须在设置中开启授权码授权

    @Value("${email.password}")
    private String emailPWD;   //授权密码

    @Value("${email.smtpserver}")
    private String smtpserver;


    @Override
    public IPage<SysNotifyManageVO> getNotifyManagePage(Page page, Map<String, Object> map) {
        return baseMapper.getNotifyManage(page,map);
    }


    @Override
    public BaseResponse addSysNotifyManageByType(SysNotifyManageRequest notiRequest) throws Exception{
        List<Long> userIds = notiRequest.getUserIds();
        if(userIds == null){
            return new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"请选择通知人");
        }
//        Integer localPlatform = notiRequest.getLocalPlatform()==null?0:notiRequest.getLocalPlatform();
//        Integer mail = notiRequest.getMail()==null?0:notiRequest.getMail();
//        Integer sms = notiRequest.getSms()==null?0:notiRequest.getSms();

//        if(localPlatform == 1){
//            SysNotificationRequest sysNotificationRequest = new SysNotificationRequest();
//            BeanUtils.copyProperties(notiRequest, sysNotificationRequest);
//            sysNotificationRequest.setMessage(notiRequest.getTitle());
//            sysNotificationService.addNotifToUserByType(sysNotificationRequest);
//        }
//
//        if(mail == 1){
//            String usersEmail = userMapper.getUsersEmail(notiRequest.getUserIds());
//            EmailUtil.sendEmail(notiRequest.getTitle(), notiRequest.getDetail(), emailPersonal, smtpserver, emailAccount, emailPWD, usersEmail);
//        }
//
//        if(sms == 1){
//            String userPhones = userMapper.getUsersPhone(notiRequest.getUserIds());
//            String[] phones =userPhones.split(",");
//            List<String> list = Arrays.asList(phones);
//            qxtTextMsgUtils.sendTextMsg(StringUtils.isBlank(notiRequest.getDetail())?notiRequest.getTitle():notiRequest.getDetail()
//                    ,list);
//        }

        List<SysNotifyManage> notifyManageList = new ArrayList<>();
        for (Long userId : userIds) {
            SysNotifyManage sysNotifyManage = new SysNotifyManage();
            BeanUtils.copyProperties(notiRequest, sysNotifyManage);

            sysNotifyManage.setId(SnowFlakeIdGenerator.getInstance().nextId());
            sysNotifyManage.setNotifyUser(userId);
            sysNotifyManage.setCreateUser(Sign.getUserId());
            sysNotifyManage.setCreateTime(DateUtil.getNow());
            notifyManageList.add(sysNotifyManage);
        }
        this.saveBatch(notifyManageList);
        return new BaseResponse(ResultEnum.DEFAULT_SUCCESS);
  }


}

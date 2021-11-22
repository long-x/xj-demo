package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.client.IaasAlertClient;
import com.ecdata.cmp.iaas.client.IaasSangforClient;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforResponse;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.dto.request.SysNoteSangforRequest;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.entity.SysNotification;
import com.ecdata.cmp.user.entity.SysNotificationReceiver;
import com.ecdata.cmp.user.entity.SysNotifyManage;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.*;
import com.ecdata.cmp.user.service.*;
import com.ecdata.cmp.user.utils.EmailUtil;
import com.ecdata.cmp.user.utils.QXTTextMsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-05 16:33
 */
@Slf4j
@Service
public class SysNotificationServiceImpl extends ServiceImpl<SysNotificationMapper, SysNotification> implements SysNotificationService {
    @Value("${role.name.tenant_admin}")
    private String tenantAdminName;

    @Autowired
    private SysNotificationReceiverService sysNotiCeivService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private SysBusinessGroupMemberMapper busiGroupMemberMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    IaasAlertClient iaasAlertClient;

    @Autowired
    IaasSangforClient iaasSangforClient;

    @Autowired
    private SysNotifyManageService sysNotifyManageService;
    @Autowired
    private IUserService userService;

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


    public boolean isExistHuaweiMessage(Long huaweiId) {
        QueryWrapper<SysNotification> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysNotification::getCsn, huaweiId);

        return baseMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<SysNotificationVO> getUserNotification(Long userId, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if(status != null){
            map.put("status", status);
        }
        return baseMapper.getUserNotification(map);
    }

    @Override
    public IPage<SysNotificationVO> getUserNotificationPage(Page page, Map<String, Object>  map) {
        return baseMapper.getUserNotification(page,map);
    }

    @Override
    public BaseResponse updateBatchUserMessageStatus(Long userId, String remark){

        //通知表  先处理非华为告警的 通知消息
        baseMapper.updateNotifiStatus(userId,2, "noExist", null);
        //接收表  标记非华为告警的 个人消息
        baseMapper.updateReceiverMessageStatus(null, userId, remark);

        List<Long> messageIds = baseMapper.getUserHuaweiMessageId(userId);
        if(messageIds != null && messageIds.size() > 0){
            //特殊处理：华为告警
            LongListResponse baseResponse = iaasAlertClient.updateBatch(AuthContext.getAuthz(), messageIds);
            if(baseResponse.getCode() == ResultEnum.DEFAULT_SUCCESS.getCode()){
                if( baseResponse.getData() != null && baseResponse.getData().size()>0){
                    //通知表
                    baseMapper.updateNotifiStatus(userId,3, null, baseResponse.getData());
                }else {
                    if(CollectionUtils.isNotEmpty(baseResponse.getData())){
                        messageIds = messageIds.stream().filter(item -> !baseResponse.getData().contains(item)).collect(toList());
                    }
                    baseMapper.updateNotifiStatus(userId,2, null, messageIds);
                }

                //接收表 记录备注
                baseMapper.updateReceiverRemark(messageIds, remark);

                return new BaseResponse(ResultEnum.DEFAULT_SUCCESS);

            }else {
                return new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(), "华为告警消息处理失败");

            }
        }

        return new BaseResponse(ResultEnum.DEFAULT_SUCCESS);
    }

    /*
    * describe: 通知若绑定华为告警，当前方案为异步处理告警通知。
    * */
    @Override
    public boolean oneDealMessage(Long id, Long updateUser, String remark){
        try{
            SysNotification sysNotification = baseMapper.selectByReceivId(id);
            if(sysNotification == null){
                return false;
            }
            //特殊处理：华为告警消息
            if(sysNotification.getCsn() != null){
                LongListResponse baseResponse = iaasAlertClient.updateBatch(AuthContext.getAuthz(),
                        Arrays.asList(sysNotification.getCsn()));
                if(baseResponse.getCode() != ResultEnum.DEFAULT_SUCCESS.getCode()){
                    return false;
                }
                if( baseResponse.getData() != null && baseResponse.getData().size()>0){
                    //消息状态 ：处理中
                    baseMapper.updateOneNotifiStatus(sysNotification.getId(), updateUser,3);
                }else {
                    //消息状态 ：已处理
                    baseMapper.updateOneNotifiStatus(sysNotification.getId(), updateUser,2);
                }
                //接收表 记录备注
                baseMapper.updateReceiverRemark(Arrays.asList(sysNotification.getCsn()), remark);
                return true;
            }else if (sysNotification.getAlertPlatform()==2 && sysNotification.getSangId() !=null){ //深信服安全云告警
                SangforResponse sangforResponse = iaasSangforClient.find(sysNotification.getSangId());
                if(sangforResponse.getCode()!= ResultEnum.DEFAULT_SUCCESS.getCode())
                    return false;
                if( sangforResponse.getData() != null) {
                    //铃铛告警处理
                    sysNotification.setRemark(remark);
                    sysNotification.setStatus(2);
                    sysNotification.setOperator(updateUser);
                    sysNotification.setOperationTime(DateUtil.getNow());
                    baseMapper.updateById(sysNotification);
                    //微服务iaas处理安全云告警
                    SangforSecurityRiskVO riskVO = sangforResponse.getData();
                    riskVO.setDealComment(remark);
                    //转给iaas处理告警
                    iaasSangforClient.update(riskVO);
                }
                return true;
            }

            //华为告警消息 ：已处理
            baseMapper.updateOneNotifiStatus(sysNotification.getId(), updateUser,2);
            //接收表  个人单条消息
            baseMapper.updateReceiverMessageStatus(id, updateUser, remark);
            return  true;
        }catch (Exception e){
            log.debug("oneDealMessage 异常"+e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dealSangforNotify(Long id, Long updateUser, String remark){
        try {
            SysNotification sysNotification = baseMapper.selectByReceivId(id);
            if (sysNotification == null) {
                return false;
            }
            if (sysNotification.getAlertPlatform() == 2 && sysNotification.getSangId() != null) {
                SangforResponse sangforResponse = iaasSangforClient.find(sysNotification.getSangId());
                if (sangforResponse.getCode() != ResultEnum.DEFAULT_SUCCESS.getCode())
                    return false;
                if (sangforResponse.getData() != null) {
                    sysNotification.setRemark(remark);
                    sysNotification.setStatus(2);
                    sysNotification.setOperator(updateUser);
                    sysNotification.setOperationTime(DateUtil.getNow());
                    baseMapper.updateById(sysNotification);
                }
                return true;
            }
            return true;
        }catch (Exception e){
            log.debug("dealSangforNotify 异常"+e);
            return false;
        }
    }


    @Override
    public Integer datchBealMessage(String ids, Long updateUser, String remark){
        if(StringUtils.isBlank(ids)){
            return null;
        }
        int count =0;
        String[] str = ids.split(",");
        for(int i=0; i<str.length; i++){
            Boolean result = oneDealMessage(Long.valueOf(str[i]), updateUser, remark);
            if(!result){
                count++;
            }
        }
        return count;
    }

    public Boolean removeOne(Long id, Long updateUser) {
        log.info("删除单个通知 sysNotification id：{}", id);
        if (SqlHelper.delBool(this.baseMapper.deleteById(id))) {
            baseMapper.modifyUpdateRecord(id, updateUser);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public BaseResponse addNotificationToUser(SysNotificationRequest sysNotificationRequest){
        SysNotification sysNotification = new SysNotification();
        BeanUtils.copyProperties(sysNotificationRequest, sysNotification);

        sysNotification.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(sysNotificationRequest.getCreateUser())
                .setCreateTime(DateUtil.getNow());
        if(sysNotification.getCsn() != null && sysNotification.getCsn() > 0){
            if(isExistHuaweiMessage(sysNotification.getCsn())){
                return new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"该条来自华为的信息已存在");
            }
        }

        log.info("addNotificationToUser "+sysNotification);
        if(sysNotification.getStatus()==null)
            sysNotification.setStatus(1);
        if (baseMapper.insert(sysNotification)>0){//SqlHelper.delBool()
            List<Long> userIds = sysNotificationRequest.getUserIds();
            if(userIds != null || userIds.size()>0){
                Set<Long> ids = new HashSet<>(userIds);
                Set<Long> failIds = new HashSet<>();
                ids = ids.stream().filter(e -> e>0).collect(Collectors.toSet());
                for(Long id :ids){

                    try {
                        SysNotificationReceiver notiCeiv = new SysNotificationReceiver();
                        notiCeiv.setId(SnowFlakeIdGenerator.getInstance().nextId())
                                .setNotificationId(sysNotification.getId())
                                .setUserId(id)
                                .setCreateUser(sysNotificationRequest.getCreateUser())
                                .setCreateTime(DateUtil.getNow());
                        sysNotiCeivService.save(notiCeiv);
                    }catch (Exception e){
                        failIds.add(id);
                        log.debug("下发员工信息失败，id{}",id);
                    }
                }
                if(failIds.size()>0){
                    return BaseResponse.builder().code(ResultEnum.DEFAULT_FAIL.getCode())
                            .message("有"+failIds.size()+"位员工消息发送失败失败,id为："+failIds.toString())
                            .build();
                }
                return new BaseResponse(ResultEnum.DEFAULT_SUCCESS);
            }

        }
        return new BaseResponse(ResultEnum.DEFAULT_FAIL);
    }


    @Override
    public BaseResponse addNotifToUserByType(SysNotificationRequest notiRequest){
        List<Long> userIds = notiRequest.getUserIds();;
        Set<Long> ids = new HashSet<>(userIds);
        if(CollectionUtils.isNotEmpty(notiRequest.getRoleIds())){
           List<Long> roleUsers = roleService.qryUserIdByRoleIds(notiRequest.getRoleIds());
            if(roleUsers != null && roleUsers.size() > 0){
                ids.addAll(roleUsers);
            }
        }
        if(CollectionUtils.isNotEmpty(notiRequest.getGroupIds())){
            List<Long> groupUsers = busiGroupMemberMapper.getMemberByIds(notiRequest.getGroupIds());
            if(groupUsers != null && groupUsers.size() > 0){
                ids.addAll(groupUsers);
            }
        }
        if(CollectionUtils.isNotEmpty(notiRequest.getDepartmentIds())){
            List<Long> departUsers = departmentMapper.qryUserIdByDepartIds(notiRequest.getDepartmentIds());
            if(departUsers != null && departUsers.size() > 0){
                ids.addAll(departUsers);
            }
        }
        if(CollectionUtils.isNotEmpty(notiRequest.getProjectIds())){
            List<Long> proUsers = projectMapper.qryUserIdByProjectIds(notiRequest.getProjectIds());
            if(proUsers != null && proUsers.size() > 0){
                ids.addAll(proUsers);
            }
        }
        notiRequest.setUserIds(new ArrayList<>(ids));
        log.info("addNotifToUserByType "+notiRequest);
        return addNotificationToUser(notiRequest);

    }


    @Override
    public List<Long> getAllCsn() {
        return baseMapper.getAllCsn();
    }

    /**
     *
     * @param notifiVO  获取到的告警vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> addNotifToBell(SysNotificationVO notifiVO) {
        //配置中查到的所有人id
        List<Long> noteUserIds = new ArrayList<>();
        Integer platform = notifiVO.getAlertPlatform();
        String level = notifiVO.getLevel();
        //获取平台和告警级别下的配置信息
        QueryWrapper<SysNotifyManage> smsWrapper = new QueryWrapper<>();
        smsWrapper.lambda().eq(SysNotifyManage::getAlertPlatform, platform);
        this.sqlWrapper(smsWrapper,level);
        QueryWrapper<SysNotifyManage> platformWrapper = new QueryWrapper<>();
        platformWrapper.lambda().eq(SysNotifyManage::getAlertPlatform, platform);
        this.sqlWrapper(platformWrapper,level);

        smsWrapper.lambda().eq(SysNotifyManage::getSms,1);
        // platformWrapper.lambda().eq(SysNotifyManage::getLocalPlatform,1);
        List<SysNotifyManage> manages = sysNotifyManageService.list(smsWrapper);
        List<SysNotifyManage> locals = sysNotifyManageService.list(platformWrapper);
        //对应的所有用户
        Set<Long> smsValidIds = new HashSet<>(this.findValidUserIds(manages,noteUserIds));
        Set<Long> localValidIds = new HashSet<>(this.findValidUserIds(locals,noteUserIds));
        log.info("smsValidIds "+smsValidIds);
        log.info("localValidIds "+localValidIds);
        //去重并集
        List<Long> disSms = new ArrayList<>(smsValidIds);
//        List<Long> disSms = smsValidIds.parallelStream().collect(toList());
//        List<Long> disLocal =localValidIds.parallelStream().collect(toList());
//        disSms.addAll(disLocal);
        List<Long> disList = new ArrayList<>(new HashSet<>(CollectionUtils.union(smsValidIds, localValidIds)));

        //站内全推
        log.info("站内全推 ");
        SysNoteSangforRequest sysNotificationRequest = new SysNoteSangforRequest();
        BeanUtils.copyProperties(notifiVO, sysNotificationRequest);
        if(CollectionUtils.isEmpty(disList)) {
            disList.addAll(roleService.qryUserIdByRole(Arrays.asList(tenantAdminName)));
        }
        log.info("disList "+disList);
        sysNotificationRequest.setUserIds(disList);
        sysNotificationRequest.setMessage(notifiVO.getMessage());
        sysNotificationRequest.setSangId(notifiVO.getSangId());
        this.addNotifToUserByType(sysNotificationRequest);
        //短信全推
        log.info("短信推送 "+disSms);
        if(CollectionUtils.isNotEmpty(disSms)) {
            List<String> userPhones = userMapper.getUsersPhoneList(disSms);//getUsersPhone
            log.info("短信Phones "+userPhones);
//        String[] phones =userPhones.split(",");
//        List<String> list = Arrays.asList(phones);
            if (CollectionUtils.isNotEmpty(userPhones)) {
                String platformStr = "";
                if (notifiVO.getAlertPlatform() == 1) {
                    platformStr = "华为云告警: (csn)" + notifiVO.getCsn() + "--" + notifiVO.getMessage();
                    log.info("华为云告警: " + notifiVO.getDetail());
                }
                else if (notifiVO.getAlertPlatform() == 2) {
                    platformStr = "安全云告警: " + notifiVO.getMessage() + "-- (ip)"+notifiVO.getDetail();
                    log.info("安全云告警: " + notifiVO.getDetail());
                }
                qxtTextMsgUtils.sendTextMsg(platformStr
                        , userPhones);
            }
        }
        return disList;
    }

    private List<Long> findValidUserIds(List<SysNotifyManage> manages,List<Long> noteUserIds){
        Map<Long, List<SysNotifyManage>> map = manages.stream()
                .collect(Collectors.groupingBy(SysNotifyManage::getNotifyUser));
        noteUserIds = new ArrayList(map.keySet());
        log.info("noteUserIds "+noteUserIds);
        //能对应记录的用户，有效的用户
        List<Long> validIds = new ArrayList<>();
        for(Long userId:noteUserIds){
            User user = userService.getById(userId);
            if (null != user)
                validIds.add(userId);
        }
        log.info("validIds "+validIds);
        return validIds;
    }

    private void sqlWrapper(QueryWrapper<SysNotifyManage> wrapper,String level){
        //1.紧急 2.重要 3.次要 4.提示 11.高 12.中 13.低
        switch (level) {
            case "1":
                wrapper.lambda().eq(SysNotifyManage::getUrgent, 1);
                break;
            case "2":
                wrapper.lambda().eq(SysNotifyManage::getImportant, 1);
                break;
            case "3":
                wrapper.lambda().eq(SysNotifyManage::getMinor, 1);
                break;
            case "4":
                wrapper.lambda().eq(SysNotifyManage::getPrompt, 1);
                break;
            case "11":
                wrapper.lambda().eq(SysNotifyManage::getHigh, 1);
                break;
            case "12":
                wrapper.lambda().eq(SysNotifyManage::getMedium, 1);
                break;
            case "13":
                wrapper.lambda().eq(SysNotifyManage::getLow, 1);
                break;
        }
    }


}
/*
for (SysNotifyManage item : manages) {
            Integer innerForm = item.getLocalPlatform() == null ? 0 : item.getLocalPlatform();
            Integer mail = item.getMail() == null ? 0 : item.getMail();
            Integer sms = item.getSms() == null ? 0 : item.getSms();

//            if(innerForm == 1){
//                log.info("站内推送 "+item);
//                SysNotificationRequest sysNotificationRequest = new SysNotificationRequest();
//                BeanUtils.copyProperties(notifiVO, sysNotificationRequest);
//                sysNotificationRequest.setMessage("华为云告警:"+notifiVO.getMessage());
//                this.addNotifToUserByType(sysNotificationRequest);
//            }

            if(mail == 1){
                log.info("邮箱推送 "+item);
                String usersEmail = userMapper.getUsersEmail(validIds);
                try {
                    EmailUtil.sendEmail(item.getTitle(), notifiVO.getMessage(), emailPersonal,
                            smtpserver, emailAccount, emailPWD, usersEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(sms == 1){

            }


                //map的value  平台 邮箱 短信 的值
//                List<Integer> pms = new ArrayList<>();
//                pms.add(innerForm);
//                pms.add(mail);
//                pms.add(sms);
//                Long userId = item.getNotifyUser();
//                User user = userService.getById(userId);
//                if (null != user)
//                    dictinctUserIds.put(userId, pms);
//                else dictinctUserIds.put(userId, new ArrayList<>());
                //user 是否存在
        }
 */
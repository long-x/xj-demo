package com.ecdata.cmp.huawei.schedule;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.huawei.dto.alarm.CsnsQueryResult;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.service.AlarmService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.IaasAlertClient;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.response.alert.IaasAlertListResponse;
import com.ecdata.cmp.user.client.SysNotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class AlertScheduleTask {
    @Autowired
    private IaasAlertClient iaasAlertClient;

    @Autowired
    private SysNotificationClient sysNotificationClient;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private ManageOneService manageOneService;


    @Value("${huawei.scheduled}")
    private boolean scheduled;

}
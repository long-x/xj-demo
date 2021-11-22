//package com.ecdata.cmp.huawei.utils;
//
//import com.ecdata.cmp.huawei.dto.alarm.CsnsQueryResult;
//import com.ecdata.cmp.huawei.service.AlarmService;
//import com.ecdata.cmp.iaas.client.IaasAlertClient;
//import com.ecdata.cmp.user.client.SysNotificationClient;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.DelayQueue;
//
//@Component
//@Slf4j
//public class AlartConsumer implements Runnable {
//    // 延时队列 ,消费者从其中获取消息进行消费
//    private DelayQueue<Message> queue;
//
//    public AlartConsumer  queue(DelayQueue<Message> queue) {
//        this.queue = queue;
//        return this;
//    }
//
//    @Override
//    public void run() {
//        AlarmService alarmService = (AlarmService) SpringUtil.getBean("alarmService");
////        IaasAlertClient iaasAlertClient = (IaasAlertClient) SpringUtil.getBean("iaasAlertClient");
//        while (true) {
//            try {
//                Message take = queue.take();
//                List<Long> idList = take.getId();
//                String token = take.getToken();
//                System.out.println("消费消息id：" + take.getId() + " 执行时间：" +new Date().toString());
//                //成功清除的告警
//                ArrayList<Long> successList = new ArrayList<>();
//                //仍未清除的告警
//                ArrayList<Long> failList = new ArrayList<>();
//                CsnsQueryResult currentAlarmInit = alarmService.getCurrentAlarmInit(token);
//                List<Long> csns = currentAlarmInit.getCsns();
//                //筛选出成功或者失败的，更改状态
//                for (Long failId : idList) {
//                    if (!csns.contains(failId)) {
//                        successList.add(failId);
//                    }else{
//                        failList.add(failId);
//                    }
//                }
//
//                IaasAlertClient iaasAlertClient = take.getIaasAlertClient();
//                SysNotificationClient sysNotificationClient = take.getSysNotificationClient();
//                if (CollectionUtils.isNotEmpty(successList)){
//                    //调用iaas接口
//                    iaasAlertClient.updateProcess(successList,2);
//                    log.info("调用iaas接口更新成功清除的告警");
//                    //调用user接口
//                    sysNotificationClient.updateProcess(successList,2);
//                    log.info("调用user接口更新成功清除的告警");
//                }
//                if (CollectionUtils.isNotEmpty(failList)){
//                    iaasAlertClient.updateProcess(failList,1);
//                    log.info("调用iaas接口更新失败清除的告警");
//                    sysNotificationClient.updateProcess(failList,1);
//                    log.info("调用user接口更新成功清除的告警");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

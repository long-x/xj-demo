package com.ecdata.cmp.huawei.utils;

import com.ecdata.cmp.iaas.client.IaasAlertClient;
import com.ecdata.cmp.user.client.SysNotificationClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class Message implements Delayed {
    private List<Long> id;//删除未完成的id
    private String body; // 消息内容
    private IaasAlertClient iaasAlertClient; // IaasAlertClient
    private SysNotificationClient sysNotificationClient; // IaasAlertClient
    private long excuteTime;// 延迟时长，这个是必须的属性因为要按照这个判断延时时长。
    private String token; // token

    public Message(List<Long> id, String body, IaasAlertClient iaasAlertClient, SysNotificationClient sysNotificationClient, long delayTime, String token) {
        this.id = id;
        this.body = body;
        this.iaasAlertClient = iaasAlertClient;
        this.sysNotificationClient = sysNotificationClient;
        this.excuteTime =TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
        this.token = token;
    }

    // 自定义实现比较方法返回 1 0 -1三个参数
    @Override
    public int compareTo(Delayed delayed) {
       /* Message msg = (Message) delayed;
        return Integer.valueOf(this.id) > Integer.valueOf(msg.id) ? 1
                : (Integer.valueOf(this.id) < Integer.valueOf(msg.id) ? -1 : 0);*/
       return 0;
    }

    // 延迟任务是否到时就是按照这个方法判断如果返回的是负数则说明到期否则还没到期
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }
}

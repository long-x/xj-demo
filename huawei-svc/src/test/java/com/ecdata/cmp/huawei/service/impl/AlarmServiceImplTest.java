package com.ecdata.cmp.huawei.service.impl;

import com.ecdata.cmp.huawei.dto.alarm.Record;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AlarmServiceImplTest {


    @Autowired
    private AlarmServiceImpl alarmService;
    @Test
    public void test (){
        String token = "x-tdmmvvaptgpjpgmk4a5c86rt9e9fulinfuhhk51hgbde9fpdhfsblgios99e2rqpml45sbljmqph06dh7xapnzuogac8nu07nv2krw5i3yhic70ak5jw5eio9gtcnwrz";
        try {
            List<Record> currentAlarm = alarmService.getCurrentAlarm(token);
            log.info("currentAlarm ={}",currentAlarm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test2(){
        String token = "x-tdmmvvaptgpjpgmk4a5c86rt9e9fulinfuhhk51hgbde9fpdhfsblgios99e2rqpml45sbljmqph06dh7xapnzuogac8nu07nv2krw5i3yhic70ak5jw5eio9gtcnwrz";
        List <Long> list = new ArrayList<>();
        list.add(17321032l);
        list.add(17284428l);
        list.add(17240811l);
        try {
            log.info("ssss ={}", alarmService.removeAlarms(token,list));
        } catch (IOException e) {

        }
    }


}
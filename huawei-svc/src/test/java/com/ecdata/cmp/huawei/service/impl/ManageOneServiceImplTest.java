package com.ecdata.cmp.huawei.service.impl;

import com.ecdata.cmp.huawei.dto.alarm.CsnsQueryResult;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.dto.token.TokenInfoDTO;
import com.ecdata.cmp.huawei.service.AlarmService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ManageOneServiceImplTest {
    @Autowired
    private ManageOneService manageOneService;

    @Autowired
    private AlarmService alarmService;

    @Test
    public void getTokenInfo() throws Exception {
        TokenInfoDTO tokenInfoDTO = TokenInfoDTO.builder().
                userName("huacun").
                value("Huawei@123").build();
        String token = manageOneService.getTokenInfo(tokenInfoDTO, "172.16.59.40:26335");
        System.out.println(token);
    }

    @Test
    public void getOMToken() throws Exception {
        OMToken token = manageOneService.getOMToken("huacun","Huawei@123");
        System.out.println(token);
    }

    @Test
    public void getMOToken() throws Exception {
        String token = manageOneService.getOCToken("huacun","Huawei@123","mo_bss_admin","");
        System.out.println(token);
    }

    @Test
    public void getCurrentAlarmByOccurUtcTime() throws Exception {
        /*CsnsQueryResult currentAlarmByOccurUtcTime = alarmService.getCurrentAlarmByOccurUtcTime("x-8ahjmrfwfyrxnsrxs5epjuhh2nc5eq2r3x2l5g3u07tjo53wlj0a2quruliqbwfwo4qpfxmr0a07fxenby7scbgapg7z5hc5qnlfnuakqp075gsbk8kb6mlhilsao8ju", "1576575300000");
        System.out.println(currentAlarmByOccurUtcTime);*/
        alarmService.getHistoryAlarm("x-6mo6fuhfbz3svyan1elhfw5g489h6o479htcfuarfyoavsil6rjy6ojz4athen7yhjkak82o6ndfapjvuotjhirxdiam0a5eo6vwdj1jmr5ctj1i5go8de6r0b6rsapc",591928l);

  /*      ArrayList<Long> longs = new ArrayList<>();
        longs.add((long) 979892);*/
//        alarmService.removeAlarmsResponse("x-byc63thiqns8kbfxg7vt9g8689qkarfzdfo8rwbspgulmkc4c84avx2mlg44jyphmnupfsars89ek7qmfu2rjw6l2m1jo4052rvwikntak2pmqnurxuoqok46ko59din",longs);
    }

   /* @Test
    public void getResIdByVM() throws Exception {
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(new Double(562958543421441L));
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(186143);
        integers.add(166589);
        List<Record> alarmByCsns = alarmService.getAlarmByCsns("x-6rlevs47antd6mftdjqrbzentjg4k70b9emnpj85team5ddd3w1c87urc8pggbc7bz7wgbrwmrardh2qpiqpg984k6aotj1d05bt6lsaips9um5c7yk647hdcbg55h85", integers);
        System.out.println(alarmByCsns);
    }*/
}
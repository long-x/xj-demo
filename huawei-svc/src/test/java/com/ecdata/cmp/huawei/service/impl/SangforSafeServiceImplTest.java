package com.ecdata.cmp.huawei.service.impl;

import com.ecdata.cmp.huawei.service.SangforSafeService;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SangforSafeServiceImplTest {

    @Autowired
    private SangforSafeService sangforSafeService;

    @Test
    public void getRiskBusinessTest() throws Exception {
        System.out.println(sangforSafeService.getRiskBusiness(1577808000L, 1586411019L, 10));
    }

    @Test
    public void getRiskTerminalTest() throws Exception {
        System.out.println(sangforSafeService.getRiskTerminal(1577808000L, 1586411019L, 10));
    }

    @Test
    public void getRisEventTest() throws Exception {
        System.out.println(sangforSafeService.getRiskEvent(1577808000L, 1586411019L, 10));
    }

    @Test
    public void getWeakPasswordTest() throws Exception {
        System.out.println(sangforSafeService.getWeakPassword(1577808000L, 1586411019L, 10));
    }

    @Test
    public void getPlainTextTest() throws Exception {
        System.out.println(sangforSafeService.getPlainText(1577808000L, 1586411019L, 10));
    }

    @Test
    public void getHoleTest() throws Exception {
        System.out.println(sangforSafeService.getHole(1577808000L, 1586411019L, 10));
    }

    @Test
    public void dealTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        SangforSecurityRiskVO vo = new SangforSecurityRiskVO();
        vo.setBranchId("14");
        vo.setBranchName("城投水务");
        vo.setDealStatus(0);
        vo.setEventKey("pwd_audit|501001|admin");
        vo.setGroupId("4273281096");
        vo.setGroupName("10.94.111.66");
        vo.setIp("10.94.111.66");
        vo.setLastTime(sdf.parse("2020-04-23 09:41:40"));
        vo.setSeverityLevel(2);
        vo.setLevel(null);
        vo.setPriority(null);
        vo.setRecordDate(20200414L);
        vo.setRiskType(4);
        vo.setType(2);
        vo.setRuleId("501001");
        vo.setDealComment("laj test2");

        /*
        SangforSecurityRiskVO vo = new SangforSecurityRiskVO();
        vo.setBranchId("0");
        vo.setBranchName("-");
        vo.setDealStatus(0);
        vo.setEventKey(null);
        vo.setGroupId("311768903");
        vo.setGroupName("-");
        vo.setIp("10.233.0.30");
        vo.setLastTime(sdf.parse("2020-04-27 00:00:00"));
        vo.setSeverityLevel(2);
        vo.setLevel(null);
        vo.setPriority(null);
        vo.setRecordDate(20200429L);
        vo.setRiskType(1);
        vo.setType(2);
        vo.setRuleId(null);
        vo.setDealComment("laj test");
         */
        System.out.println(sangforSafeService.dealRisk(vo));
    }
}
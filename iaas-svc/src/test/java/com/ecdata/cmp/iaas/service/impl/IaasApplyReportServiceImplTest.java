package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.iaas.entity.dto.report.CalculationResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceAssignedStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceStatisticsVO;
import com.ecdata.cmp.iaas.service.IaasApplyReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/5/11 14:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IaasApplyReportServiceImplTest {

    @Autowired
    private IaasApplyReportService reportService;

    @Test
    public void report() {
        List<CloudResourceStatisticsVO> report = reportService.cloudResourceStatistics("2","");

        System.out.println("");
    }

    @Test
    public void cloudResourceAssignedStatistics() {
        Map<String, Object> parm=new HashMap<>();
        List<CloudResourceAssignedStatisticsVO> report = reportService.cloudResourceAssignedStatistics(parm);

        System.out.println("");
    }

    @Test
    public void calculationResourceStatistics() {
        Map<String, Object> parm=new HashMap<>();
        List<CalculationResourceStatisticsVO> report = reportService.calculationResourceStatistics(parm);

        System.out.println("");
    }

    @Test
    public void Test(){
        List<Map<String, String>> list = reportService.cloudResourceAssignedStatistics2();
        log.info("list --{}",list);
    }


}
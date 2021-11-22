package com.ecdata.cmp.iaas.mapper.report;

import com.ecdata.cmp.iaas.entity.dto.distribution.CpuAndCountVO;
import com.ecdata.cmp.iaas.entity.dto.distribution.DeptCountVO;
import com.ecdata.cmp.iaas.entity.dto.report.IaasApplyReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author xuxiaojian
 * @date 2020/5/11 11:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IaasApplyReportMapperTest {
    @Autowired
    private IaasApplyReportMapper applyReportMapper;
    @Test
    public void queryReport() {
//        List<IaasApplyReport> iaasApplyReports = applyReportMapper.queryReport();
        String projectId= "39105364583038978";
        List<DeptCountVO> deptCount = applyReportMapper.getDeptCount(projectId);
        log.info("deptCount --{} ",deptCount);
//        List<CpuAndCountVO> vmDisk = applyReportMapper.getVmDisk(projectId);
//        log.info("vmDisk --{} ",vmDisk);
//        List<CpuAndCountVO> vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectId);
//        log.info("vmCpuAndCount --{} ",vmCpuAndCount);
//        List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectId);
//        log.info("bareMetalCount --{} ",bareMetalCount);

    }
}
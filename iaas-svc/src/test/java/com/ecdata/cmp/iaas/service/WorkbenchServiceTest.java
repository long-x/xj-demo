package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserBusinessGroupResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.VirtualMachineCapacityVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkbenchServiceTest {

    @Autowired
    private WorkbenchService workbenchService;

    @Test
    public void queryUserBusinessGroupResourceStatistics() {
        Sign.setCurrentTenantId(10000L);
        List<UserBusinessGroupResourceVO> list = workbenchService.queryUserBusinessGroupResourceStatistics(new ArrayList<>());

        System.out.println("ss");
    }

    @Test
    public void testTree() {
        Sign.setCurrentTenantId(10000L);
        List<BusinessGroupResourceStatisticsVO> businessGroupResourceStatisticsVOS = workbenchService.queryBusinessGroupResourceStatistics(new ArrayList<>());
    }


    @Test
    public void hostDistributionPieDataMap() {
        Sign.setCurrentTenantId(10000L);
        workbenchService.hostDistributionPieDataMap(new ArrayList<>());
    }

    @Test
    public void hostDistributionPieDataMapNew() {
        Sign.setCurrentTenantId(10000L);
        workbenchService.hostDistributionPieDataMapNew(new ArrayList<>());
    }

    @Test
    public void queryUserBusinessGroupResourceStatisticsNew() {
        Sign.setCurrentTenantId(10000L);
        workbenchService.queryUserBusinessGroupResourceStatisticsNew(new ArrayList<>());
    }

    @Test
    public void queryVirtualMachineCapacity() {
        Sign.setCurrentTenantId(10000L);
        List<VirtualMachineCapacityVO> virtualMachineCapacityVOS = workbenchService.queryVirtualMachineCapacity(null);

        System.out.println("sss");
    }
}
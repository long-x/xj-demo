package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xxj
 * @date 2020/2/9 22:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProviderServiceImplTest {
    @Autowired
    private ProviderServiceImpl providerService;

    @Test
    public void saveVM() {
        Sign.setCurrentTenantId(10000L);
        providerService.saveVM(34956266983923715l, 39105364583038978l, 1111l, null, null,
                39213176793387009l, create(), null);
    }

    private VirtualMachineVO create() {
        return VirtualMachineVO.builder().status("active")
                .cpuUsage(2.05F)
                .diskUsage("4.18")
                .bizRegionName("城投私有云")
                .azoneId("811370A5736C39E1A2155BB3296136DC")
                .bizRegionId("853FFE094A6D339E97E84AD6BF06A531")
                .memoryUsage(5.51F)
                .projectId("49f1b96e3fd34860a70de9cb85607ebe")
                .floatingIp("10.233.0.17")
                .supportHistoryPerf(true)
                .azoneName("az1.dc1")
                .privateIps("172.17.2.10")
                .flavor("16U128G|16vCPU|128GB")
                .vdcName("vdc_JT")
                .physicalHostId("A94D2276E41232778E0E9919831E6C4E")
                .osVersion("Windows Server 2012 R2 Standard 64bit")
                .nativeId("25091977-df1a-4b21-899b-72b750fbfb7a")
                .build();
    }
}
package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.request.VDCRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VSphereRequest;
import com.ecdata.cmp.iaas.service.ProviderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class VSphereServiceImplTest {
    @Autowired
    ProviderService vSphereService;

    @Test
    public void contextLoads() {
        String data = "4U16G|4vCPU|16GB";
        String split = data.split("\\|")[0];
        String cpu = split.substring(0, split.indexOf("U"));
        String mn = split.substring(split.indexOf("U")+1, split.indexOf("G"));

        System.out.println("");
    }

    @Test
    public void syncVSphereData() throws IOException {
        Sign.setCurrentTenantId(10000L);
        VSphereRequest request = new VSphereRequest();
        request.setAddress("vc.cloud.local");
        request.setUsername("administrator@vsphere.local");
        request.setPassword("P@ssw0rd");
        request.setSyncType("vsphere");
        request.setProviderId(3l);
        vSphereService.syncVSphereData(request);
    }

    @Test
    public void syncVDCData() throws IOException {
        Sign.setCurrentTenantId(10000L);
        VDCRequest request = new VDCRequest();
        request.setAddress("vc.cloud.local");
        request.setUsername("administrator@vsphere.local");
        request.setPassword("P@ssw0rd");
        vSphereService.syncVDCData(request);
    }
}
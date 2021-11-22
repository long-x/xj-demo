package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.service.IIaasProviderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IIaasProviderServiceImplTest {

    @Autowired
    private IIaasProviderService iaasProviderService;

    @Test
    public void queryHuaWeiResourceInfo() {
        Sign.setCurrentTenantId(10000L);
        iaasProviderService.queryHuaWeiResourceInfo(34956266983923715L);
    }
}
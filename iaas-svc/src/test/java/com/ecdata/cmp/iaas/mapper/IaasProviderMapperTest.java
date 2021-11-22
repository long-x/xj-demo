package com.ecdata.cmp.iaas.mapper;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.response.provider.cascade.ProviderCascade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xxj
 * @date 2020/2/11 15:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IaasProviderMapperTest {

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    @Test
    public void queryProviderByAreaId() {
        Sign.setCurrentTenantId(10000L);
        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryProviderByAreaId(39102702777122823l);
        System.out.println();
    }

    @Test
    public void queryProviderCascadeByProvider() {
        Sign.setCurrentTenantId(10000L);
        ProviderCascade providerCascade = iaasProviderMapper.queryProviderCascadeByProvider(34956266983923715L);
        System.out.println();
    }
}
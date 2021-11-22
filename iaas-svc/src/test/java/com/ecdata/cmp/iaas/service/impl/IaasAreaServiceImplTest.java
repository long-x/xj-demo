package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.IaasArea;
import com.ecdata.cmp.iaas.entity.dto.IaasAreaVO;
import com.ecdata.cmp.iaas.service.IaasAreaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/16 10:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IaasAreaServiceImplTest {

    @Autowired
    private IaasAreaService iaasAreaService;

    @Test
    public void queryIaasAreas() {
        Sign.setCurrentTenantId(10000L);
        List<IaasAreaVO> iaasAreas = iaasAreaService.queryIaasAreas();
    }
}
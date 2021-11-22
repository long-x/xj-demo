package com.ecdata.cmp.iaas.mapper;

import com.ecdata.cmp.common.crypto.Sign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author hhj
 * @create created in 15:29 2020/5/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IaasAreaSumMapperTest {
    @Autowired
    private IaasAreaSumMapper iaasAreaSumMapper;

    @Test
    public void getByServerNameAndAreaName() {
//        Sign.setCurrentTenantId(10000L);
         iaasAreaSumMapper.queryServerNameAndAreaName("vCPU", "生产区虚拟机");

    }
}

package com.ecdata.cmp.iaas.mapper.apply;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/9 11:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IaasApplyResourceMapperTest {
    @Autowired
    private IaasApplyResourceMapper iaasApplyResourceMapper;

    @Test
    public void queryApplyResource() {
        Sign.setCurrentTenantId(10000L);
        List<IaasApplyResourceVO> iaasApplyResourceVOS = iaasApplyResourceMapper.queryApplyResource(69028012670586885L);

        System.out.printf("");
    }
}
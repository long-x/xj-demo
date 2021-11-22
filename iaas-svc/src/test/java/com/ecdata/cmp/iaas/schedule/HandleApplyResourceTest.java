package com.ecdata.cmp.iaas.schedule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author hhj
 * @create created in 10:07 2020/5/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HandleApplyResourceTest {
    @Autowired
    private HandleApplyResource handleApplyResource;

    @Test
    public void handleLeaseExpireDataTest() {
        handleApplyResource.handleLeaseExpireData();
        System.err.println("=====OK=====");
    }
}

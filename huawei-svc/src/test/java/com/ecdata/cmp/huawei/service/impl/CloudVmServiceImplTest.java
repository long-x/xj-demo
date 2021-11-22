package com.ecdata.cmp.huawei.service.impl;

import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.huawei.service.CloudVmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudVmServiceImplTest {

    @Autowired
    private CloudVmService  cloudVmService;


    @Test
    public void test(){

        CloudVmVO vo = new CloudVmVO();
        vo.setAvailabilityZone("az5.dc1");
        vo.setName("xj-server-01");
        vo.setImageRef("53e3aaa8-9783-4551-b7c8-62a1454845aa");
        vo.setSize("100");
        vo.setVolumetype("business_type_01");
        vo.setFlavorRef("86e7aa1c-14fb-48b6-a6eb-ef064425b003");
        vo.setVpcid("06c813f7-127b-4d74-9855-3371024e53f2");
        vo.setId("53ef7423-f478-4017-8890-4fde9d1bb984");
        vo.setAdminPass("12f8e18AAA");
        vo.setCount("1");

        vo.setSubnetId("0a978b7a-048c-46cb-8d2d-965365d19e13");

        cloudVmService.createVm(vo);

    }



}
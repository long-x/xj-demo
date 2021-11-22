/*
package com.ecdata.cmp.user;


import com.ecdata.cmp.user.dto.request.BimOrgReq;
import com.ecdata.cmp.user.dto.request.BimSchemaReq;
import com.ecdata.cmp.user.dto.request.BimUserReq;
import com.ecdata.cmp.user.service.IBimOrgService;
import com.ecdata.cmp.user.service.IBimSchemaService;
import com.ecdata.cmp.user.service.IBimUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

*/
/**
 * @title: test
 * @Author: shig
 * @description:
 * @Date: 2020/3/6 11:15 上午
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class BimTest {
    @Autowired
    IBimSchemaService bimSchemaService;

    @Autowired
    IBimUserService bimUserService;

    @Autowired
    IBimOrgService bimOrgService;

    @Test
    public void schemaService() {
        BimSchemaReq bimSchemaReq = new BimSchemaReq();
        bimSchemaReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimSchemaReq.setBimRemoteUser("BIMadmin");
        bimSchemaReq.setBimRemotePwd("password");
        System.out.println(bimSchemaService.schemaService(bimSchemaReq));
    }

    @Test
    public void userCreateService() {
        BimUserReq bimUserReq = new BimUserReq();
        bimUserReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimUserReq.setBimRemoteUser("BIMadmin");
        bimUserReq.setBimRemotePwd("password");
        bimUserReq.setLoginName("zhangsan");
        bimUserReq.setFullName("张三");
        bimUserReq.setOrgId("110");
        System.out.println(bimUserService.userCreateService(bimUserReq));
    }

    @Test
    public void userUpdateService() {
        BimUserReq bimUserReq = new BimUserReq();
        bimUserReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimUserReq.setBimRemoteUser("BIMadmin");
        bimUserReq.setBimRemotePwd("password");
        bimUserReq.setBimUid("67999856240717831");
        bimUserReq.setLoginName("lisi");
        bimUserReq.setFullName("李四");
        System.out.println(bimUserService.userUpdateService(bimUserReq));
    }

    @Test
    public void userDeleteService() {
        BimUserReq bimUserReq = new BimUserReq();
        bimUserReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimUserReq.setBimRemoteUser("BIMadmin");
        bimUserReq.setBimRemotePwd("password");
        bimUserReq.setBimUid("67999856240717831");
        System.out.println(bimUserService.userDeleteService(bimUserReq));
    }

    @Test
    public void queryAllUserIdsService() {
        BimUserReq bimUserReq = new BimUserReq();
        bimUserReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimUserReq.setBimRemoteUser("BIMadmin");
        bimUserReq.setBimRemotePwd("password");
        System.out.println(bimUserService.queryAllUserIdsService(bimUserReq));
    }

    @Test
    public void queryUserByIdService() {
        BimUserReq bimUserReq = new BimUserReq();
        bimUserReq.setBimRequestId("9e928d12ec8a4c1bb75283b8df71308d");
        bimUserReq.setBimRemoteUser("BIMadmin");
        bimUserReq.setBimRemotePwd("password");
        bimUserReq.setBimUid("10000");
        System.out.println(bimUserService.queryUserByIdService(bimUserReq));
    }


    @Test
    public void orgCreateService() {
        BimOrgReq bimOrgReq = new BimOrgReq();
        bimOrgReq.setBimRequestId("11928d12ec8a4c1bb75283b8df71308d");
        bimOrgReq.setBimRemoteUser("ecdataadmin");
        bimOrgReq.setBimRemotePwd("12345");
        bimOrgReq.setOrgName("集团信息中心");
        bimOrgReq.setParOrgId("29695638086189059");
        System.out.println(bimOrgService.orgCreateService(bimOrgReq));
    }

    @Test
    public void orgUpdateService() {
        BimOrgReq bimOrgReq = new BimOrgReq();
        bimOrgReq.setBimRequestId("11928d12ec8a4c1bb75283b8df71308d");
        bimOrgReq.setBimRemoteUser("ecdataadmin");
        bimOrgReq.setBimRemotePwd("12345");
        bimOrgReq.setOrgName("集团信息中心-改");
        bimOrgReq.setBimOrgId("68040150080360453");
        bimOrgReq.setParOrgId("000002");
        System.out.println(bimOrgService.orgUpdateService(bimOrgReq));
    }

    @Test
    public void orgDeleteService() {
        BimOrgReq bimOrgReq = new BimOrgReq();
        bimOrgReq.setBimRequestId("11928d12ec8a4c1bb75283b8df71308d");
        bimOrgReq.setBimRemoteUser("ecdataadmin");
        bimOrgReq.setBimRemotePwd("12345");
        bimOrgReq.setBimOrgId("68040150080360453");
        System.out.println(bimOrgService.orgDeleteService(bimOrgReq));
    }

    @Test
    public void queryAllOrgIdsService() {
        BimOrgReq bimOrgReq = new BimOrgReq();
        bimOrgReq.setBimRequestId("11928d12ec8a4c1bb75283b8df71308d");
        bimOrgReq.setBimRemoteUser("ecdataadmin");
        bimOrgReq.setBimRemotePwd("12345");
        System.out.println(bimOrgService.queryAllOrgIdsService(bimOrgReq));
    }

    @Test
    public void queryOrgByIdService() {
        BimOrgReq bimOrgReq = new BimOrgReq();
        bimOrgReq.setBimRequestId("11928d12ec8a4c1bb75283b8df71308d");
        bimOrgReq.setBimRemoteUser("ecdataadmin");
        bimOrgReq.setBimRemotePwd("12345");
        bimOrgReq.setBimOrgId("680392139494359041");
        System.out.println(bimOrgService.queryOrgByIdService(bimOrgReq));
    }

}*/

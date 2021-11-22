package com.ecdata.cmp.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.ecdata.cmp.user.dto.request.BimSchemaReq;
import com.ecdata.cmp.user.dto.response.BimSchemaChildResp;
import com.ecdata.cmp.user.dto.response.BimSchemaResp;
import com.ecdata.cmp.user.mapper.UserMapper;
import com.ecdata.cmp.user.service.IBimSchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: IBimSchemaServiceImpl
 * @Author: shig
 * @description: 账户等属性实现类
 * @Date: 2020/3/5 6:53 下午
 */
@Slf4j
@Service
public class BimSchemaServiceImpl implements IBimSchemaService {

    @Autowired
    UserMapper userMapper;

    @Override
    public BimSchemaResp schemaService(BimSchemaReq bimSchemaReq) {
        log.info("竹云/SchemaService:对象属性字段查询,请求JSON对象\n" + JSON.toJSONString(bimSchemaReq));
        BimSchemaResp bimSchemaResp = new BimSchemaResp();
        //1.账号表结构
        List<BimSchemaChildResp> childAccountResps = userMapper.bimSchemaService("sys_user");
        //1.2 表字段名转换成对象属性名称
        for (int i = 0; i < childAccountResps.size(); i++) {
            BimSchemaChildResp childResp = new BimSchemaChildResp();
            BeanUtils.copyProperties(childAccountResps.get(i), childResp);
            childResp.setName(columnNameToAttrName(childResp.getName()));
            childAccountResps.set(i, childResp);
        }
        //单独orgId:中间表，sys_user_department
        BimSchemaChildResp orgIdBimSchemaChildResp = new BimSchemaChildResp();
        orgIdBimSchemaChildResp.setName("departmentId");
        orgIdBimSchemaChildResp.setRequired(false);
        orgIdBimSchemaChildResp.setMultivalued(false);
        orgIdBimSchemaChildResp.setType("long");
        orgIdBimSchemaChildResp.setComment("组织机构id");
        childAccountResps.add(orgIdBimSchemaChildResp);
        BimSchemaChildResp[] userAccountRespArr = childAccountResps.toArray(new BimSchemaChildResp[childAccountResps.size()]);
        bimSchemaResp.setAccount(userAccountRespArr);

        //2.组织结构
        List<BimSchemaChildResp> childOrgResps = userMapper.bimSchemaService("sys_department");
        //2.2 表字段名转换成对象属性名称
        for (int i = 0; i < childOrgResps.size(); i++) {
            BimSchemaChildResp childResp = new BimSchemaChildResp();
            BeanUtils.copyProperties(childOrgResps.get(i), childResp);
            childResp.setName(columnNameToAttrName(childResp.getName()));
            if("parentIdsStr".equals(childResp.getName())){
                childResp.setMultivalued(true);
            }
            childOrgResps.set(i, childResp);
        }
        BimSchemaChildResp[] orgRespArr = childOrgResps.toArray(new BimSchemaChildResp[childOrgResps.size()]);
        bimSchemaResp.setOrganization(orgRespArr);

        //3.请求id
        bimSchemaResp.setBimRequestId(bimSchemaReq.getBimRequestId());

        //4.返回json字符串
        log.info("竹云/SchemaService:对象属性字段查询,响应JSON对象\n" + JSON.toJSONString(bimSchemaResp));


        return bimSchemaResp;
    }

    private static String columnNameToAttrName(String columnName) {
        if (columnName == null) return columnName;
        if (columnName.contains("_")) {
            char[] ch = columnName.toCharArray();
            for (int index = 0; index < ch.length; index++) {
                if (ch[index] == '_' && index > 0 && index < ch.length - 1) {
                    if (ch[index + 1] >= 'a' && ch[index + 1] <= 'z') {
                        ch[index + 1] = (char) (ch[index + 1] - 32);
                    }
                }
            }

            return new String(ch).replace("_", "");
        }
        return columnName;
    }
}
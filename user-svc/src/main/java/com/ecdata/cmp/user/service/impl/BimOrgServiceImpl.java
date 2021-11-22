package com.ecdata.cmp.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.request.BimOrgReq;
import com.ecdata.cmp.user.dto.response.BimOrgOrganizationResp;
import com.ecdata.cmp.user.dto.response.BimOrgResp;
import com.ecdata.cmp.user.entity.Department;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.DepartmentMapper;
import com.ecdata.cmp.user.mapper.UserDepartmentMapper;
import com.ecdata.cmp.user.mapper.UserMapper;
import com.ecdata.cmp.user.service.IBimOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title: BimOrgServiceImpl
 * @Author: shig
 * @description: BimOrgService实现类
 * @Date: 2020/3/5 8:04 下午
 */
@Slf4j
@Service
public class BimOrgServiceImpl implements IBimOrgService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public BimOrgResp orgCreateService(BimOrgReq bimOrgReq) {
        log.info("竹云/orgCreateService:组织机构创建,请求JSON对象\n" + JSON.toJSONString(bimOrgReq));
        BimOrgResp bimOrgResp = new BimOrgResp();
        bimOrgResp.setBimRequestId(bimOrgReq.getBimRequestId());
        try {
            Department department = new Department();
            Long uid = SnowFlakeIdGenerator.getInstance().nextId();
            BeanUtils.copyProperties(bimOrgReq, department);
            department.setId(uid);
            //获取登录账户id
            User user = getUserIdByName(bimOrgReq);
            if (user != null) {
                department.setCreateUser(user.getId());
            }
            if(getOrgByName(bimOrgReq)!=null){
                bimOrgResp.setResultCode("500");
                bimOrgResp.setMessage("组织名称重复，账号创建失败");
                log.info("组织名称重复，账号创建失败");
                return bimOrgResp;
            }
            department.setCreateTime(new Date());
            department.setRemark("竹云");
            departmentMapper.insert(department);
            bimOrgResp.setOrgId(uid.toString());
            bimOrgResp.setResultCode("0");
            bimOrgResp.setMessage("success");
            log.info("竹云/orgCreateService:组织机构创建,响应JSON对象\n" + JSON.toJSONString(bimOrgResp));
        } catch (Exception e) {
            bimOrgResp.setResultCode("500");
            bimOrgResp.setMessage("字段不全，组织机构创建失败");
            log.error("字段不全，组织机构创建失败");
        }
        return bimOrgResp;
    }

    private User getUserIdByName(BimOrgReq bimOrgReq) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("is_deleted", 0);
        query.eq("name", bimOrgReq.getBimRemoteUser());
        return userMapper.selectOne(query);
    }

    /**
     * 创建组织 名称不能重复
     * @param bimOrgReq
     * @return
     */
    private Department getOrgByName(BimOrgReq bimOrgReq){
        QueryWrapper<Department> query = new QueryWrapper<>();
        query.eq("is_deleted", 0);
        query.eq("department_name", bimOrgReq.getDepartmentName());
//        query.eq("department_alias", bimOrgReq.getDepartmentAlias());
        query.eq("parent_id", bimOrgReq.getParentId());
        return departmentMapper.selectOne(query);
    }


    @Override
    public BimOrgResp orgUpdateService(BimOrgReq bimOrgReq) {
        log.info("竹云/orgUpdateService:组织机构修改,请求JSON对象\n" + JSON.toJSONString(bimOrgReq));
        BimOrgResp bimOrgResp = new BimOrgResp();
        bimOrgResp.setBimRequestId(bimOrgReq.getBimRequestId());
        try {
            Department dbDepartment = departmentMapper.selectById(Long.parseLong(bimOrgReq.getBimOrgId()));
            if (dbDepartment != null) {
                Department department = new Department();
                BeanUtils.copyProperties(bimOrgReq, department);
                department.setId(Long.parseLong(bimOrgReq.getBimOrgId()));
                //获取登录账户id
                User user = getUserIdByName(bimOrgReq);
                if (user != null) {
                    department.setUpdateUser(user.getId());
                }
                department.setUpdateTime(new Date());
                departmentMapper.updateById(department);

                bimOrgResp.setResultCode("0");
                bimOrgResp.setMessage("success");
                log.info("竹云/orgUpdateService:组织机构修改,响应JSON对象\n" + JSON.toJSONString(bimOrgResp));
            } else {
                bimOrgResp.setResultCode("200");
                bimOrgResp.setMessage("没有查询到该组织机构");
                log.error("没有查询到该组织机构");
            }
        } catch (Exception e) {
            bimOrgResp.setResultCode("500");
            bimOrgResp.setMessage("字段不全，组织机构修改失败");
            log.error("字段不全，组织机构修改失败");
        }
        return bimOrgResp;
    }

    @Override
    public BimOrgResp orgDeleteService(BimOrgReq bimOrgReq) {
        log.info("竹云/orgDeleteService:组织机构删除,请求JSON对象\n" + JSON.toJSONString(bimOrgReq));
        BimOrgResp bimOrgResp = new BimOrgResp();
        bimOrgResp.setBimRequestId(bimOrgReq.getBimRequestId());
        try {
            Department department = new Department();
            department.setId(Long.parseLong(bimOrgReq.getBimOrgId()));
            departmentMapper.deleteById(department);
            bimOrgResp.setResultCode("0");
            bimOrgResp.setMessage("success");
            log.info("竹云/orgDeleteService:组织机构删除,响应JSON对象\n" + JSON.toJSONString(bimOrgResp));
        } catch (Exception e) {
            bimOrgResp.setResultCode("500");
            bimOrgResp.setMessage("字段不全，组织机构删除失败");
            log.error("字段不全，组织机构删除失败");
        }
        return bimOrgResp;
    }

    @Override
    public BimOrgResp queryAllOrgIdsService(BimOrgReq bimOrgReq) {
        log.info("竹云/queryAllOrgIdsService:批量查询组织机构,请求JSON对象\n" + JSON.toJSONString(bimOrgReq));
        BimOrgResp bimOrgResp = new BimOrgResp();
        bimOrgResp.setBimRequestId(bimOrgReq.getBimRequestId());
        try {
            QueryWrapper<Department> query = new QueryWrapper<>();
            query.eq("is_deleted", 0);
            List<Department> departments = departmentMapper.selectList(query);
            if (departments.size() > 0) {
                List<String> orgIds = new ArrayList<>();
                for (Department department : departments) {
                    orgIds.add(department.getId().toString());
                }
                bimOrgResp.setOrgIdList(orgIds);
                bimOrgResp.setResultCode("0");
                bimOrgResp.setMessage("success");
                log.info("竹云/queryAllOrgIdsService:批量查询组织机构,响应JSON对象\n" + JSON.toJSONString(bimOrgResp));
            } else {
                bimOrgResp.setResultCode("202");
                bimOrgResp.setMessage("没有查询到该组织机构");
                log.info("没有查询到该组织机构");
            }
        } catch (Exception e) {
            bimOrgResp.setResultCode("500");
            bimOrgResp.setMessage(e.toString());
            log.error(e.toString());
        }
        return bimOrgResp;
    }

    @Override
    public BimOrgResp queryOrgByIdService(BimOrgReq bimOrgReq) {
        log.info("竹云/queryOrgByIdService:根据组织机构 ID 查询组织机构详细内容,请求JSON对象\n" + JSON.toJSONString(bimOrgReq));
        BimOrgResp bimOrgResp = new BimOrgResp();
        bimOrgResp.setBimRequestId(bimOrgReq.getBimOrgId());
        try {
            Department department = departmentMapper.selectById(Long.parseLong(bimOrgReq.getBimOrgId()));
            if (department != null) {
                BimOrgOrganizationResp bimOrgOrganizationResp = new BimOrgOrganizationResp();
                bimOrgOrganizationResp.setId(Long.parseLong(bimOrgReq.getBimOrgId()));
                BeanUtils.copyProperties(department,bimOrgOrganizationResp);
                if (department.getParentId() != null) {
                    bimOrgOrganizationResp.setParentId(department.getParentId());
                }
                //组织机构子类
                bimOrgResp.setOrganization(bimOrgOrganizationResp);
                bimOrgResp.setResultCode("0");
                bimOrgResp.setMessage("success");
                log.info("竹云/queryOrgByIdService:根据组织机构 ID 查询组织机构详细内容,响应JSON对象\n" + JSON.toJSONString(bimOrgResp));
            } else {
                bimOrgResp.setResultCode("202");
                bimOrgResp.setMessage("没有查询到该组织机构");
                log.info("没有查询到该组织机构");
            }
        } catch (Exception e) {
            bimOrgResp.setResultCode("500");
            bimOrgResp.setMessage(e.toString());
            log.error(e.toString());
        }
        return bimOrgResp;
    }
}
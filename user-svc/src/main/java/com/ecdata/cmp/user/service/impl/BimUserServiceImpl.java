package com.ecdata.cmp.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.request.BimUserReq;
import com.ecdata.cmp.user.dto.response.BimUserAccountResp;
import com.ecdata.cmp.user.dto.response.BimUserResp;
import com.ecdata.cmp.user.entity.*;
import com.ecdata.cmp.user.mapper.*;
import com.ecdata.cmp.user.service.IBimUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title: BimUserServiceImpl
 * @Author: shig
 * @description: BimUserService实现类
 * @Date: 2020/3/5 8:04 下午
 */
@Slf4j
@Service
public class BimUserServiceImpl implements IBimUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SysBusinessGroupDepartmentMapper sysBusinessGroupDepartmentMapper;

    @Autowired
    private SysBusinessGroupMemberMapper sysBusinessGroupMemberMapper;

    @Override
    public BimUserResp userCreateService(BimUserReq bimUserReq) {
        log.info("竹云/userCreateService:账号创建,请求JSON对象\n" + JSON.toJSONString(bimUserReq));
        //0.返回对象
        BimUserResp bimUserResp = new BimUserResp();
        bimUserResp.setBimRequestId(bimUserReq.getBimRequestId());
        try {
            //1.账户-->用户
            User user = new User();
            Long uid = SnowFlakeIdGenerator.getInstance().nextId();
            BeanUtils.copyProperties(bimUserReq, user);
            user.setId(uid);

            //查询 登录账户id
            User user1 = getUserCreateByName(bimUserReq);
            if (user1 != null) {
                user.setCreateUser(user1.getId());
            }
            //创建用户需要查询是否登录名称冲突
            if(getUserByName(bimUserReq)!=null){
                bimUserResp.setResultCode("500");
                bimUserResp.setMessage("用户名重复，账号创建失败");
                log.info("用户名重复，账号创建失败");
                return bimUserResp;
            }

            user.setCreateTime(new Date());
            userMapper.insert(user);

            //2.用户和部门关联表:sys_user_department
            UserDepartment userDepartment = new UserDepartment();
            Long departId = SnowFlakeIdGenerator.getInstance().nextId();
            userDepartment.setId(departId);
            userDepartment.setUserId(uid);
            if (bimUserReq.getDepartmentId() != null) {
                long departmentId = Long.parseLong(bimUserReq.getDepartmentId());
                userDepartment.setDepartmentId(departmentId);
                //拿到部门id 查找对应业务组的id
                SysBusinessGroupDepartment department = sysBusinessGroupDepartmentMapper.getGroupIdByDepartment(departmentId);
                //关联业务组与用户
                if (department.getBusinessGroupId() != null){
                    SysBusinessGroupMember member = new SysBusinessGroupMember();
                    member.setBusinessGroupId(department.getBusinessGroupId());
                    member.setUserId(uid);
                    member.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    member.setCreateTime(DateUtil.getNow());
                    sysBusinessGroupMemberMapper.insert(member);
                }
            }
            userDepartment.setCreateTime(new Date());
            userDepartmentMapper.insert(userDepartment);
            bimUserResp.setUid(uid.toString());

            //3.用户角色
            UserRole userRole = new UserRole();
            userRole.setId(SnowFlakeIdGenerator.getInstance().nextId());
            userRole.setUserId(uid);
            //3.2 角色id：根据 role_name:default_user及 is_delete:0 查询 sys_role表， id
            QueryWrapper<Role> query = new QueryWrapper<>();
            query.eq("role_name", "default_user");
            query.eq("is_deleted", 0);
            Role role = roleMapper.selectOne(query);
            if (role != null) {
                userRole.setRoleId(role.getId());
            } else {
                userRole.setRoleId(10000L);
            }
            userRole.setCreateTime(new Date());
            userRoleMapper.insert(userRole);
            bimUserResp.setResultCode("0");
            bimUserResp.setMessage("success");
            log.info("竹云/userCreateService:账号创建,响应JSON对象\n" + JSON.toJSONString(bimUserResp));
        } catch (Exception e) {
            bimUserResp.setResultCode("500");
            bimUserResp.setMessage("字段不全，账号创建失败");
            log.info("字段不全，账号创建失败");
        }
        return bimUserResp;
    }

    private User getUserCreateByName(BimUserReq bimUserReq) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("is_deleted", 0);
        query.eq("name", bimUserReq.getBimRemoteUser());
        return userMapper.selectOne(query);
    }

    /**
     * 用户名和显示名不能重名
     * @param bimUserReq
     * @return
     */
    private User getUserByName(BimUserReq bimUserReq){
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("is_deleted", 0);
        query.eq("name", bimUserReq.getName()).or();
        query.eq("display_name",bimUserReq.getDisplayName());


        return userMapper.selectOne(query);
    }


    @Override
    public BimUserResp userUpdateService(BimUserReq bimUserReq) {
        log.info("竹云/userUpdateService:账号修改,请求JSON对象\n" + JSON.toJSONString(bimUserReq));
        BimUserResp bimUserResp = new BimUserResp();
        bimUserResp.setBimRequestId(bimUserReq.getBimRequestId());
        try {
            //1.查询是否存在
            User dbUser = userMapper.selectById(Long.parseLong(bimUserReq.getBimUid()));
            if (dbUser != null) {
                User user = new User();
                BeanUtils.copyProperties(bimUserReq, user);
                user.setId(Long.parseLong(bimUserReq.getBimUid()));

                //2.查询 登录账户id
                User user1 = getUserCreateByName(bimUserReq);
                if (user1 != null) {
                    user.setUpdateUser(user1.getId());
                }
                user.setUpdateTime(new Date());
                userMapper.updateById(user);

                //3.用户部门表:竹云 一个账户一个部门
                QueryWrapper<UserDepartment> query = new QueryWrapper<>();
                query.eq("user_id", Long.parseLong(bimUserReq.getBimUid()));
                UserDepartment dbUserDepartment = userDepartmentMapper.selectOne(query);
                if (dbUserDepartment != null) {
                    dbUserDepartment.setDepartmentId(Long.parseLong(bimUserReq.getDepartmentId()));
                    userDepartmentMapper.updateById(dbUserDepartment);
                }
                bimUserResp.setResultCode("0");
                bimUserResp.setMessage("success");
                log.info("竹云/userUpdateService:账号修改,响应JSON对象\n" + JSON.toJSONString(bimUserResp));
            } else {
                bimUserResp.setResultCode("200");
                bimUserResp.setMessage("没有查询到该账号id");
                log.info("没有查询到该账号id");
            }
        } catch (Exception e) {
            bimUserResp.setResultCode("500");
            bimUserResp.setMessage("字段不全，账号修改失败");
            log.info("字段不全，账号修改失败");
        }
        return bimUserResp;
    }

    @Override
    public BimUserResp userDeleteService(BimUserReq bimUserReq) {
        log.info("竹云/userDeleteService:账号删除,请求JSON对象\n" + JSON.toJSONString(bimUserReq));
        BimUserResp bimUserResp = new BimUserResp();
        bimUserResp.setBimRequestId(bimUserReq.getBimRequestId());
        try {
            //1.用户删除
            userMapper.deleteById(Long.parseLong(bimUserReq.getBimUid()));
            //2.用户部门删除:根据用户id
            QueryWrapper<UserDepartment> query = new QueryWrapper<>();
            query.eq("user_id", Long.parseLong(bimUserReq.getBimUid()));
            int count = userDepartmentMapper.selectCount(query);
            if (count > 0) {
                userDepartmentMapper.delete(query);
            }
            bimUserResp.setResultCode("0");
            bimUserResp.setMessage("success");
            log.info("竹云/userDeleteService:账号删除,响应JSON对象\n" + JSON.toJSONString(bimUserResp));
        } catch (Exception e) {
            bimUserResp.setResultCode("500");
            bimUserResp.setMessage("字段不全，账号删除失败");
            log.info("字段不全，账号删除失败");
        }
        return bimUserResp;
    }

    @Override
    public BimUserResp queryAllUserIdsService(BimUserReq bimUserReq) {
        log.info("竹云/queryAllUserIdsService:批量查询账号,请求JSON对象\n" + JSON.toJSONString(bimUserReq));
        BimUserResp bimUserResp = new BimUserResp();
        bimUserResp.setBimRequestId(bimUserReq.getBimRequestId());
        try {
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("is_deleted", 0);
            List<User> userList = userMapper.selectList(query);
            if (userList.size() > 0) {
                List<String> uids = new ArrayList<>();
                for (User user1 : userList) {
                    uids.add(user1.getId().toString());
                }
                bimUserResp.setUserIdList(uids);
                bimUserResp.setResultCode("0");
                bimUserResp.setMessage("success");
                log.info("竹云/queryAllUserIdsService:批量查询账号,响应JSON对象\n" + JSON.toJSONString(bimUserResp));
            } else {
                bimUserResp.setResultCode("202");
                bimUserResp.setMessage("没有查询到所有账户");
                log.info("没有查询到所有账户");
            }
        } catch (Exception e) {
            bimUserResp.setResultCode("500");
            bimUserResp.setMessage(e.toString());
        }
        return bimUserResp;
    }

    @Override
    public BimUserResp queryUserByIdService(BimUserReq bimUserReq) {
        log.info("竹云/queryUserByIdService:根据账号 ID 查询账号详细内容,请求JSON对象\n" + JSON.toJSONString(bimUserReq));
        BimUserResp bimUserResp = new BimUserResp();
        bimUserResp.setBimRequestId(bimUserReq.getBimRequestId());
        try {
            User user = userMapper.selectById(bimUserReq.getBimUid());
            if (user != null) {
                BimUserAccountResp userAccountResp = new BimUserAccountResp();
                QueryWrapper<UserDepartment> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(UserDepartment::getUserId, bimUserReq.getBimUid());
                UserDepartment userDepartment = userDepartmentMapper.selectOne(queryWrapper);
                if (userDepartment != null) {
                    userAccountResp.setDepartMentId(userDepartment.getDepartmentId().toString());
                }
                BeanUtils.copyProperties(user, userAccountResp);
                userAccountResp.setUid(bimUserReq.getBimUid());
                //账户
                bimUserResp.setAccount(userAccountResp);
                bimUserResp.setResultCode("0");
                bimUserResp.setMessage("success");
                log.info("竹云/queryUserByIdService:根据账号 ID 查询账号详细内容,响应JSON对象\n" + JSON.toJSONString(bimUserResp));
            } else {
                bimUserResp.setResultCode("202");
                bimUserResp.setMessage("没有查询到该组织机构");
                log.info("没有查询到该组织机构");
            }
        } catch (Exception e) {
            bimUserResp.setResultCode("500");
            bimUserResp.setMessage(e.toString());
        }
        return bimUserResp;
    }
}
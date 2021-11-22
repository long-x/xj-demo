package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.entity.Role;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.RoleMapper;
import com.ecdata.cmp.user.service.IRoleService;
import com.ecdata.cmp.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IUserService userService;

    @Override
    public List<RoleVO> qryRoleByUserId(Long userId) {
        return baseMapper.qryRoleByUserId(userId);
    }

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public List<Long> qryUserIdByRole(List<String> roleNameList) {
        return baseMapper.qryUserIdByRole(roleNameList);
    }

    @Override
    public List<RoleVO> transform(List<Role> roleList) {
        List<RoleVO> roleVOList = new ArrayList<>();
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(role, roleVO);
                roleVOList.add(roleVO);
            }
        }
        return roleVOList;
    }

    @Override
    public List<RoleVO> listAdmin() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getRoleName, "sys_admin").or().eq(Role::getRoleName, "tenant_admin");
        List<Role> roleList = this.list(queryWrapper);
        return this.transform(roleList);
    }

    @Override
    public List<User> getITDirectors(Long roleId) {
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        List<Long> list = baseMapper.qryUserIdByRoleIds(roleIdList);
        List<User> userList = new ArrayList<>();
        for (Long userId : list) {
            User user = userService.getById(userId);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public List<Long> qryUserIdByRoleIds(List<Long> roleIds){
        return baseMapper.qryUserIdByRoleIds(roleIds);
    }
}

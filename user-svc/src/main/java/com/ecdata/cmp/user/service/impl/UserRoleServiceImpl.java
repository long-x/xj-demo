package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.RoleVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.UserRole;
import com.ecdata.cmp.user.mapper.UserRoleMapper;
import com.ecdata.cmp.user.service.IUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-19
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public void insertBatch(Long userId, List<RoleVO> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return;
        }
        LambdaQueryWrapper<UserRole> query = new LambdaQueryWrapper<>();
        for (RoleVO role : roleList) {
            Long roleId = role.getId();
            query.eq(UserRole::getRoleId, roleId);
            query.eq(UserRole::getUserId, userId);
            List<UserRole> depList = baseMapper.selectList(query);
            if (depList == null || depList.size() == 0) {
                baseMapper.insert(new UserRole(SnowFlakeIdGenerator.getInstance().nextId(), userId, roleId, DateUtil.getNow()));
            }
        }
    }

    @Override
    public void updateUserRole(Long userId, List<RoleVO> roleList) {
        LambdaQueryWrapper<UserRole> query = new LambdaQueryWrapper<>();
        query.eq(UserRole::getUserId, userId);
        baseMapper.delete(query);
        for (int i = 0; roleList != null && i < roleList.size(); i++) {
            baseMapper.insert(new UserRole(SnowFlakeIdGenerator.getInstance().nextId(), userId, roleList.get(i).getId(), DateUtil.getNow()));
        }
    }
}

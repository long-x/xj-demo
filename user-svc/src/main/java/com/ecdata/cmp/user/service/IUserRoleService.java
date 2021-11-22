package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.UserRole;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-19
 */
public interface IUserRoleService extends IService<UserRole> {


    /**
     * 批量插入用户角色
     * @param userId      用户id
     * @param roleList    角色列表
     */
    void insertBatch(Long userId, List<RoleVO> roleList);
    /**
     * 批量更新用户角色
     * @param userId      用户id
     * @param roleList    角色列表
     */
    void updateUserRole(Long userId, List<RoleVO> roleList);

}

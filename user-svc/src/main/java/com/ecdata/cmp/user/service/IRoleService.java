package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.entity.Role;
import com.ecdata.cmp.user.entity.User;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
public interface IRoleService extends IService<Role> {
    /**
     * 根据用户id查询角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<RoleVO> qryRoleByUserId(Long userId);

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 获取拥有至少其一角色的用户id列表
     *
     * @param roleNameList 角色名列表
     * @return 用户id列表
     */
    List<Long> qryUserIdByRole(List<String> roleNameList);

    List<Long> qryUserIdByRoleIds(List<Long> roleIds);

    /**
     * 转换角色列表
     *
     * @param roleList 角色列表
     * @return List<RoleVO>
     */
    List<RoleVO> transform(List<Role> roleList);

    /**
     * 获取管理员角色
     *
     * @return List<RoleVO>
     */
    List<RoleVO> listAdmin();

    List<User> getITDirectors(Long roleId);
}

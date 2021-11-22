package com.ecdata.cmp.user.service;


import com.ecdata.cmp.user.dto.PermissionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-04-26
 */
public interface IPermissionService extends IService<Permission> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 获取树形权限菜单(排除按钮权限)
     *
     * @return List<PermissionVO>
     */
    List<PermissionVO> getTree();

    /**
     * 获取树形权限菜单和id集合
     *
     * @return List<PermissionVO>
     */
    Map<String, List> getTreeAndIds();

    /**
     * 查询用户菜单权限
     *
     * @param userId 用户id
     * @return List<Permission>
     */
    List<Permission> queryByUser(Long userId);

    /**
     * 查询用户按钮权限
     *
     * @param userId 用户id
     * @return List<Permission>
     */
    List<PermissionVO> queryButtonByUser(Long userId);

    /**
     * 查询按钮权限
     *
     * @return List<Permission>
     */
    List<Permission> queryButton();

    /**
     * 添加菜单权限
     *
     * @param permission 菜单权限
     * @return true成功，false失败
     */
    boolean addPermission(Permission permission);

    /**
     * 修改菜单权限
     *
     * @param permission 菜单权限
     * @return true成功，false失败
     */
    boolean updatePermission(Permission permission);

    /**
     * 删除菜单权限
     *
     * @param id 菜单权限id
     * @return true成功，false失败
     */
    boolean removePermission(Long id);

    /**
     * 获取有权限的角色名
     *
     * @param permissionId 菜单权限id
     * @return List<String>
     */
    List<String> getRoleNameByPermissionId(Long permissionId);

    /**
     * 获取初始化时候的菜单信息
     *
     * @param type 类型 1：顶级租户(包含子租户内容)   2：子租户
     * @return List<Permission>
     */
    List<Permission> getInitPermission(Integer type);
}

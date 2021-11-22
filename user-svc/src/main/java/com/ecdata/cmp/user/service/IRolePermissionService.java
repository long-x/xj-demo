package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.RolePermission;

/**
 * @author xuxinsheng
 * @since 2019-04-26
*/
public interface IRolePermissionService extends IService<RolePermission> {
    /**
     * 保存授权（先删后增）
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     */
    void saveRolePermission(Long roleId, String permissionIds);
}

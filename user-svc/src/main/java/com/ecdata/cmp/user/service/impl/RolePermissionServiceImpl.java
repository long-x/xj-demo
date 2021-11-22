package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.RolePermission;
import com.ecdata.cmp.user.mapper.RolePermissionMapper;
import com.ecdata.cmp.user.service.IRolePermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-26
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {
    @Override
    public void saveRolePermission(Long roleId, String permissionIds) {
        LambdaQueryWrapper<RolePermission> query = new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId);
        this.remove(query);
        List<RolePermission> list = new ArrayList<>();
        String[] arr = permissionIds.split(",");
        for (String pId : arr) {
            if (StringUtils.isNotEmpty(pId)) {
                Long id = SnowFlakeIdGenerator.getInstance().nextId();
                RolePermission rolePermission = new RolePermission(id, roleId, Long.parseLong(pId));
                list.add(rolePermission);
            }
        }
        this.saveBatch(list);
    }
}

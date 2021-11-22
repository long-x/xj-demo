package com.ecdata.cmp.user.service.impl;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.PermissionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.Permission;
import com.ecdata.cmp.user.mapper.PermissionMapper;
import com.ecdata.cmp.user.service.IPermissionService;
import com.ecdata.cmp.user.utils.PermissionUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-04-26
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Override
    public List<PermissionVO> getTree() {
        LambdaQueryWrapper<Permission> query = new LambdaQueryWrapper<>();
        query.orderByAsc(Permission::getSortNo);
        List<Permission> list = this.list(query);
        return PermissionUtil.getTree(list);
    }

    @Override
    public Map<String, List> getTreeAndIds() {
        final int size = 2;
        Map<String, List> map = Maps.newHashMapWithExpectedSize(size);
        List<Long> ids = new ArrayList<>();
        LambdaQueryWrapper<Permission> query = new LambdaQueryWrapper<>();
        query.orderByAsc(Permission::getSortNo);
        List<Permission> list = this.list(query);
        List<PermissionVO> tree = new ArrayList<>();
        List<PermissionVO> childList = new ArrayList<>();
        for (Permission permission : list) {
            ids.add(permission.getId());
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            Long id = permission.getId();
            permissionVO.setKey(id.toString());
            permissionVO.setValue(id.toString());
            if (null == PermissionUtil.handleParentId(permissionVO.getParentId())) {
                tree.add(permissionVO);
            } else {
                childList.add(permissionVO);
            }
        }
        PermissionUtil.setChildren(tree, childList);
        map.put("tree", tree);
        map.put("ids", ids);
        return map;
    }

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public List<Permission> queryByUser(Long userId) {
        return baseMapper.queryByUser(userId);
    }

    @Override
    public List<PermissionVO> queryButtonByUser(Long userId) {
        return baseMapper.queryButtonByUser(userId);
    }

    @Override
    @Cacheable(value = "permission")
    public List<Permission> queryButton() {
        final int menuType = 2;
        return this.list(new QueryWrapper<Permission>().lambda().eq(Permission::getMenuType, menuType));
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true, condition = "#permission.menuType==2")
    public boolean addPermission(Permission permission) {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if (permission.getMenuType() == 0) {
            permission.setParentId(null);
        }
        //----------------------------------------------------------------------
        Long pid = permission.getParentId();
        if (pid != null) {
            //设置父节点不为叶子节点
            this.baseMapper.setMenuLeaf(pid, 0);
        }
        permission.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setIsLeaf(true)
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        return this.save(permission);
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true, condition = "#permission.menuType==2")
    public boolean updatePermission(Permission permission) {
        Permission oldPermission = this.getById(permission.getId());

        if (oldPermission == null) {
            return false;
        }

        //Step1.判断是否是一级菜单，是的话清空父菜单ID
        if (permission.getMenuType() == 0) {
            permission.setParentId(0L);
        }
        //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
        int count = this.count(new QueryWrapper<Permission>().lambda().eq(Permission::getParentId, permission.getId()));
        if (count == 0) {
            permission.setIsLeaf(true);
        }
        if (!this.updateById(permission)) {
            return false;
        }

        //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
        Long nowPid = PermissionUtil.handleParentId(permission.getParentId());
        Long oldPid = PermissionUtil.handleParentId(oldPermission.getParentId());
        boolean changed = (nowPid != null && !nowPid.equals(oldPid)) || (nowPid == null && oldPid != null);
        if (changed) {
            //a.设置新的父菜单不为叶子节点
            if (nowPid != null) {
                this.baseMapper.setMenuLeaf(nowPid, 0);
            }
            //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
            if (oldPid != null) {
                int num = this.count(new QueryWrapper<Permission>().lambda().eq(Permission::getParentId, oldPid));
                if (num == 0) {
                    this.baseMapper.setMenuLeaf(oldPid, 1);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "permission", allEntries = true)
    public boolean removePermission(Long id) {
        Permission permission = this.getById(id);
        if (permission == null) {
            return false;
        }
        Long pid = permission.getParentId();
        int count = this.count(new QueryWrapper<Permission>().lambda().eq(Permission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            this.baseMapper.setMenuLeaf(pid, 1);
        }
        this.baseMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenByParentId(permission.getId());
        return true;
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @param parentId 父id
     */
    private void removeChildrenByParentId(Long parentId) {
        LambdaQueryWrapper<Permission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(Permission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<Permission> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (Permission p : permissionList) {
                Long id = p.getId();
                int num = this.count(new LambdaQueryWrapper<Permission>().eq(Permission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenByParentId(id);
                }
            }
        }
    }

    @Override
    public List<String> getRoleNameByPermissionId(Long permissionId) {
        return baseMapper.getRoleNameByPermissionId(permissionId);
    }

    @Override
    public List<Permission> getInitPermission(Integer type) {
        return baseMapper.getInitPermission(type);
    }
}

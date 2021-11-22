package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Hash;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.paas.client.PaasSvcClient;
import com.ecdata.cmp.user.dto.PermissionVO;
import com.ecdata.cmp.user.dto.TenantVO;
import com.ecdata.cmp.user.entity.Permission;
import com.ecdata.cmp.user.entity.Role;
import com.ecdata.cmp.user.entity.RolePermission;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import com.ecdata.cmp.user.entity.Tenant;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.entity.UserRole;
import com.ecdata.cmp.user.mapper.TenantMapper;
import com.ecdata.cmp.user.service.IPermissionService;
import com.ecdata.cmp.user.service.IRolePermissionService;
import com.ecdata.cmp.user.service.ISysBusinessGroupMemberService;
import com.ecdata.cmp.user.service.ISysBusinessGroupService;
import com.ecdata.cmp.user.service.ITenantService;
import com.ecdata.cmp.user.service.IUserService;
import com.ecdata.cmp.user.utils.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Slf4j
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    /**
     * 权限Service
     */
    @Autowired
    private IPermissionService permissionService;
    /**
     * 角色权限Service
     */
    @Autowired
    private IRolePermissionService rolePermissionService;
    /**
     * 业务组服务
     */
    @Autowired
    private ISysBusinessGroupService sysBusinessGroupService;
    /**
     * 业务组成员服务
     */
    @Autowired
    private ISysBusinessGroupMemberService sysBusinessGroupMemberService;
    /**
     * paas服务客户端
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PaasSvcClient paasSvcClient;
    /**
     * userService
     */
    @Autowired
    private IUserService userService;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public IPage<TenantVO> qryTenantInfo(Page page, String keyword) {
        return baseMapper.qryTenantInfo(page, keyword);
    }

    @Override
    public void initUser(Tenant tenant) {
        try {
            Sign.setCurrentTenantId(tenant.getId());

            Long userId = SnowFlakeIdGenerator.getInstance().nextId();
            User user = new User().setDisplayName("管理员").setName("admin")
                    .setPassword(Hash.encode("123456"))
                    .setCreateUser(userId)
                    .setCreateTime(DateUtil.getNow())
                    .setId(userId);
            user.insert();

            Role role = new Role().setRoleName("tenant_admin").setRoleAlias("租户管理员")
                    .setCreateUser(userId)
                    .setCreateTime(DateUtil.getNow())
                    .setId(SnowFlakeIdGenerator.getInstance().nextId());
            role.insert();

            UserRole userRole = new UserRole(SnowFlakeIdGenerator.getInstance().nextId(), user.getId(), role.getId(), DateUtil.getNow());
            userRole.insert();

            User sysAdmin = userService.getSysAdmin();
            if (sysAdmin != null) {
                UserRole sysAdminRole = new UserRole(SnowFlakeIdGenerator.getInstance().nextId(), sysAdmin.getId(), role.getId(), DateUtil.getNow());
                sysAdminRole.insert();
            }

            Integer type = tenant.getParentId() == null ? 1 : Constants.TWO;
            List<Permission> permissionList = permissionService.getInitPermission(type);
            List<PermissionVO> tree = PermissionUtil.getTree(permissionList);
            this.addInitPermission(tree, null, userId, role.getId());

            SysBusinessGroup sysBusinessGroup = new SysBusinessGroup();
            sysBusinessGroup.setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setBusinessGroupName("default(默认业务组)").setAdminUser(userId).setRemark("default(默认业务组)")
                    .setCreateUser(userId).setCreateTime(DateUtil.getNow());
            sysBusinessGroupService.save(sysBusinessGroup);
            SysBusinessGroupMember member = new SysBusinessGroupMember();
            member.setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setBusinessGroupId(sysBusinessGroup.getId()).setUserId(userId)
                    .setCreateUser(userId).setCreateTime(DateUtil.getNow());
            sysBusinessGroupMemberService.save(member);

            paasSvcClient.initForTenant(AuthContext.getAuthz(), tenant.getId(), userId, type);
        } catch (Exception e) {
            log.error("initUser", e);
        } finally {
            Sign.removeCurrentTenantId();
        }
    }

    private void addInitPermission(List<PermissionVO> tree, Long parentId, Long creator, Long roleId) {
        for (PermissionVO permissionVO : tree) {
//            所有租户统一菜单
//            Permission permission = new Permission();
//            BeanUtils.copyProperties(permissionVO, permission);
//            permission.setId(SnowFlakeIdGenerator.getInstance().nextId()).setTenantId(null).setParentId(parentId).setInitType(0)
//                    .setCreateUser(creator).setCreateTime(DateUtil.getNow()).setUpdateUser(null).setUpdateTime(null);
//            permission.insert();

//            Long permissionId = permission.getId();
            Long permissionId = permissionVO.getId();
            RolePermission rolePermission = new RolePermission(SnowFlakeIdGenerator.getInstance().nextId(), roleId, permissionId);
            rolePermission.insert();

            List<PermissionVO> children = permissionVO.getChildren();
            if (children != null && children.size() > 0) {
                addInitPermission(children, permissionId, creator, roleId);
            }
        }
    }

}

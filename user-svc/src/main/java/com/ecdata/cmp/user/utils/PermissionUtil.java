package com.ecdata.cmp.user.utils;

import com.ecdata.cmp.user.dto.PermissionVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.user.entity.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-28
 */
public final class PermissionUtil {

    private PermissionUtil() {

    }

    /**
     * 斜线
     */
    private static final String SLASH = "/";
    /**
     * views/
     */
    private static final String VIEW_SLASH = "views/";
    /**
     * src/views/
     */
    private static final String SRC_SLASH_VIEW_SLASH = "src/views/";
    /**
     * .vue
     */
    private static final String DOT_VUE = ".vue";
    /**
     * http
     */
    private static final String HTTP = "http";
    /**
     * http://
     */
    private static final String HTTP_COLON_SLASH_SLASH = "http://";
    /**
     * https://
     */
    private static final String HTTPS_COLON_SLASH_SLASH = "https://";
    /**
     * IframePageView
     */
    private static final String IFRAME_PAGE_VIEW = "IframePageView";

    /**
     * 智能处理数据，减少用户失误操作
     *
     * @param permission 菜单权限
     * @return Permission
     */
    public static Permission intelligentPermission(Permission permission) {
        if (permission == null) {
            return null;
        }
        // 组件
        if (permission.getComponent() != null) {
            String component = permission.getComponent();
            if (component.startsWith(SLASH)) {
                component = component.substring(1);
            }
            if (component.startsWith(VIEW_SLASH)) {
                component = component.replaceFirst(VIEW_SLASH, "");
            }
            if (component.startsWith(SRC_SLASH_VIEW_SLASH)) {
                component = component.replaceFirst(SRC_SLASH_VIEW_SLASH, "");
            }
            if (component.endsWith(DOT_VUE)) {
                component = component.replace(DOT_VUE, "");
            }
            permission.setComponent(component);
        }
        // 请求URL
        if (permission.getPath() != null) {
            String path = permission.getPath();
            if (path.endsWith(DOT_VUE)) {
                path = path.replace(DOT_VUE, "");
            }
            if (!path.startsWith(HTTP) && !path.startsWith(SLASH)) {
                path = SLASH + path;
            }
            permission.setPath(path);
        }

        if (StringUtils.isEmpty(permission.getName())) {
            permission.setName(pathToRouteName(permission.getPath()));
        }

        // 一级菜单默认组件
        if (0 == permission.getMenuType() && permission.getComponent() == null) {
            // 一级菜单默认组件
            permission.setComponent("RouteView");
        }
        return permission;
    }

    /**
     * 处理父id，若父id为null或小于1视为无父id
     *
     * @param parentId 父id
     * @return null：无父id；  大于1：父id
     */
    public static Long handleParentId(Long parentId) {
        if (parentId == null || parentId < 1) {
            return null;
        } else {
            return parentId;
        }
    }

    public static JSONArray generateRouterMap(List<Permission> permissionList) {
        JSONObject home = new JSONObject();
        home.put("title", "首页");
        home.put("path", "/");
        home.put("name", "name");
        home.put("component", "BasicLayout");
        home.put("redirect", "/dashboard/workplace");
        JSONArray children = new JSONArray();
        JSONArray metaList = new JSONArray();

        final int buttonRightType = 2;
        for (Permission permission : permissionList) {
            if (permission.getMenuType() == buttonRightType) {
                continue;
            }
            JSONObject jsonObject = generateRouter(permission);
            if (PermissionUtil.handleParentId(permission.getParentId()) == null) {
                children.add(jsonObject);
            } else {
                metaList.add(jsonObject);
            }
        }
        setChildren(children, metaList);
        home.put("children", children);

        JSONArray ret = new JSONArray();
        ret.add(home);
        return ret;
    }

    private static JSONObject generateRouter(Permission permission) {
        JSONObject router = new JSONObject();
        if (permission == null) {
            return router;
        }
        router.put("id", permission.getId());
        router.put("parentId", permission.getParentId());

        final int buttonType = 2;
        if (permission.getMenuType() == buttonType) {
            router.put("action", permission.getPerms());
            router.put("describe", permission.getTitle());
        } else {
            if (StringUtils.isNotEmpty(permission.getName())) {
                router.put("name", permission.getName());
            } else {
                router.put("name", pathToRouteName(permission.getPath()));
            }

            router.put("title", permission.getTitle());
            router.put("path", permission.getPath());
            router.put("component", permission.getComponent());
            String icon = permission.getIcon();
            if (StringUtils.trimToNull(icon) != null) {
                router.put("icon", permission.getIcon());
            }
            //是否隐藏路由，默认都是显示的
            router.put("hidden", permission.getHidden());
            //是否隐藏子菜单
            router.put("hideChildrenInMenu ", permission.getHideChildrenInMenu());
            if (PermissionUtil.handleParentId(permission.getParentId()) == null) {
                //一级菜单跳转地址
                router.put("redirect", permission.getRedirect());
            }
            if (IFRAME_PAGE_VIEW.equals(permission.getComponent())) {
                router.put("url", permission.getRedirect());
            }
            if (StringUtils.isNotEmpty(permission.getIcon())) {
                router.put("icon", permission.getIcon());
            }
            if (isWWWHttpUrl(permission.getPath()) && StringUtils.isNotEmpty(permission.getTarget())) {
                router.put("target", permission.getTarget());
            }
        }
        return router;
    }

    private static String pathToRouteName(String path) {
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith(SLASH)) {
                path = path.substring(1);
            }
            path = path.replace("/", "-");
            path = path.replace(":", "@");
            return path;
        } else {
            return null;
        }
    }

    private static boolean isWWWHttpUrl(String path) {
        return path != null && (path.startsWith(HTTP_COLON_SLASH_SLASH) || path.startsWith(HTTPS_COLON_SLASH_SLASH));
    }

    private static void setChildren(JSONArray parentList, JSONArray metaList) {

        for (int i = 0; i < parentList.size(); i++) {
            JSONObject parent = parentList.getJSONObject(i);
            Long parentId = parent.getLong("id");
            JSONArray children = new JSONArray();
            Iterator it = metaList.iterator();
            while (it.hasNext()) {
                JSONObject child = (JSONObject) it.next();
                if (parentId.equals(child.getLong("parentId"))) {
                    children.add(child);
                    it.remove();
                }
            }
            if (children.size() > 0) {
                if (metaList.size() > 0) {
                    setChildren(children, metaList);
                }
            } else {
                children = null;
            }
            parent.put("children", children);
        }
    }

    public static void setChildren(List<PermissionVO> parentList, List<PermissionVO> childList) {
        for (PermissionVO parent : parentList) {
            Long parentId = parent.getId();
            List<PermissionVO> children = new ArrayList<>();
            Iterator<PermissionVO> it = childList.iterator();
            while (it.hasNext()) {
                PermissionVO child = it.next();
                if (parentId.equals(child.getParentId())) {
                    children.add(child);
                    it.remove();
                }
            }
            if (children.size() > 0) {
                if (childList.size() > 0) {
                    PermissionUtil.setChildren(children, childList);
                }
            } else {
                children = null;
            }
            parent.setChildren(children);
        }
    }

    public static List<PermissionVO> getTree(List<Permission> permissionList) {
        List<PermissionVO> tree = new ArrayList<>();
        List<PermissionVO> childList = new ArrayList<>();
        for (Permission permission : permissionList) {
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
        return tree;
    }
}

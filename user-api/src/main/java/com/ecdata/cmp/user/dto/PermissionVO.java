package com.ecdata.cmp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-26
*/
@Data
@Accessors(chain = true)
@ApiModel(value = "菜单权限", description = "菜单权限")
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 2772008221061058631L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Long parentId;

    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String title;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 组件
     */
    @ApiModelProperty(value = "组件")
    private String component;

    /**
     * 路由名称
     */
    @ApiModelProperty(value = "路由名称,且不能重名（可由path截取）")
    private String name;

    /**
     * 一级菜单跳转地址
     */
    @ApiModelProperty(value = "一级菜单跳转地址")
    private String redirect;

    /**
     * 菜单类型
     */
    @ApiModelProperty(value = "菜单类型（0：一级菜单；1：子菜单 ；2：按钮权限）")
    private Integer menuType;

    /**
     * 菜单权限编码
     */
    @ApiModelProperty(value = "菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开")
    private String perms;

    /**
     * 菜单排序
     */
    @ApiModelProperty(value = "菜单排序")
    private Integer sortNo;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 是否叶子节点
     */
    @ApiModelProperty(value = "是否叶子节点")
    private Boolean isLeaf;

    /**
     * 强制菜单显示为Item而不是SubItem
     */
    @ApiModelProperty(value = "强制菜单显示为Item而不是SubItem(配合 meta.hidden)")
    private Boolean hideChildrenInMenu;

    /**
     * 是否隐藏路由菜单
     */
    @ApiModelProperty(value = "是否隐藏路由菜单")
    private Boolean hidden;

    /**
     * 前端target属性
     */
    @ApiModelProperty(value = "前端target属性")
    private String target;

    /**
     * 初始类型
     */
    @ApiModelProperty(value = "初始类型(0:非初始菜单; 1:顶级租户管理员初始菜单; 2:子租户管理员初始菜单;)")
    private Integer initType;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /** id对应前端数据树中的key */
    @ApiModelProperty(value = "id对应前端数据树中的key")
    private String key;

    /** id对应前端数据树中的value */
    @ApiModelProperty(value = "id对应前端数据树中的value")
    private String value;

    /** 子菜单权限 */
    @ApiModelProperty(value = "子菜单权限")
    private List<PermissionVO> children;

}

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
 * @since 2019-03-29
*/
@Data
@Accessors(chain = true)
@ApiModel(value = "用户对象", description = "用户对象")
public class UserVO implements Serializable {

    private static final long serialVersionUID = -710741014879362266L;
    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 展示名称 */
    @ApiModelProperty(value = "展示名称--昵称")
    private String displayName;

    /** 用户姓名 */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /** 联系号码 */
    @ApiModelProperty(value = "联系号码")
    private String phone;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /** 账号 */
    @ApiModelProperty(value = "账号")
    private String account;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    private String password;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /** 性别 */
    @ApiModelProperty(value = "性别(0:未知;1:男;2:女;)")
    private Integer sex;

    /** 状态 */
    @ApiModelProperty(value = "状态(1:正常;2:冻结)")
    private Integer status;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /** 最后登陆时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后登陆时间")
    private Date lastLoginTime;

    /** origin_pwd */
    @ApiModelProperty(value = "origin_pwd")
    private String originPwd;


    /** source_id */
    @ApiModelProperty(value = "source_id")
    private String sourceId;

    /** source_pwd */
    @ApiModelProperty(value = "source_pwd")
    private String sourcePwd;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /** 头像 */
    @ApiModelProperty(value = "确认密码")
    private String confirm;

    /** 部门列表 */
    @ApiModelProperty(value = "部门列表")
    private List<DepartmentVO> departmentList;

    /** 展示部门 */
    @ApiModelProperty(value = "展示部门")
    private String showDepartment;

    /** 角色列表 */
    @ApiModelProperty(value = "角色列表")
    private List<RoleVO> roleList;

    /** 展示角色 */
    @ApiModelProperty(value = "展示角色")
    private String showRole;

    /** 项目列表 */
    @ApiModelProperty(value = "项目列表")
    private List<ProjectVO> projectList;

    /** 展示项目 */
    @ApiModelProperty(value = "展示项目")
    private String showProject;

    /** 权限列表 */
    @ApiModelProperty(value = "权限列表")
    private List<PermissionVO> permissionList;

}

package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "用户对象", description = "用户对象")
public class User extends Model<User> {

    private static final long serialVersionUID = -1959394579821254628L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 展示名称 */
    @ApiModelProperty(value = "展示名称")
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

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String avatar;

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

    /** source_id */
    @ApiModelProperty(value = "source_id")
    private String sourceId;

    /** source_pwd */
    @ApiModelProperty(value = "source_pwd")
    private String sourcePwd;

    /**
     * 通过注解方式和XML形式自定义的SQL不会自动加上逻辑条件
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

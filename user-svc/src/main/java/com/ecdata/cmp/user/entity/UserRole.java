package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-04-19
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role")
@ApiModel(value = "用户角色对象", description = "用户角色对象")
public class UserRole extends Model<UserRole> {

    private static final long serialVersionUID = 989862486971056741L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 角色id */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//,timezone = "GMT+8"
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public UserRole() {
    }
    public UserRole(Long id, Long userId, Long roleId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.createTime = createTime;
    }
}

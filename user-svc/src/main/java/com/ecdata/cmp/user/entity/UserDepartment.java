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
@TableName("sys_user_department")
@ApiModel(value = "用户部门对象", description = "用户部门对象")
public class UserDepartment extends Model {

    private static final long serialVersionUID = -5844300275885709266L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 部门id */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public UserDepartment() {
    }

    public UserDepartment(Long id, Long userId, Long departmentId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.departmentId = departmentId;
        this.createTime = createTime;
    }
}

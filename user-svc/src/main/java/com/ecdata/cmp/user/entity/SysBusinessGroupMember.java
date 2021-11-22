package com.ecdata.cmp.user.entity;

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
 * 业务组成员表
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:19
 * @modified By：
 */
@Data
@Accessors(chain = true)
@TableName("sys_business_group_member")
@ApiModel(value = "业务组成员对象", description = "业务组成员对象")
public class SysBusinessGroupMember extends Model<SysBusinessGroupMember> {

    private static final long serialVersionUID = 3499415065794752394L;
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

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
 * 业务组
 *
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:18
 * @modified By：
 */
@Data
@Accessors(chain = true)
@TableName("sys_business_group")
@ApiModel(value = "业务组对象", description = "业务组对象")
public class SysBusinessGroup extends Model<SysBusinessGroup> {

    private static final long serialVersionUID = 7390899738742640554L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 业务组名
     */
    @ApiModelProperty(value = "业务组名")
    private String businessGroupName;

    /**
     * 流程定义name
     */
    @ApiModelProperty(value = "流程定义name")
    private String processDefinitionName;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key")
    private String processDefinitionKey;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Long parentId;

    /**
     * 管理员
     */
    @ApiModelProperty(value = "管理员")
    private Long adminUser;

    /**
     * 默认租期
     */
    @ApiModelProperty(value = "默认租期")
    private Long defaultLease;

    /**
     * 默认租期
     */
    @ApiModelProperty(value = "租期时间")
    private Integer period;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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
     * 是否已删除(0表示未删除，1表示已删除)
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;

    /**
     * 是否表示应用
     */
    @ApiModelProperty(value = "是否表示应用(0表示是应用,1表示不是应用)")
    private Integer isApp;

    /**
     * 服务器名称前缀
     */
    @ApiModelProperty(value = "服务器名称前缀")
    private String serverNamePrefix;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

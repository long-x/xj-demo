package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @since 2019-03-21
 */
@Data
@Accessors(chain = true)
@TableName("sys_tenant")
@ApiModel(value = "租户对象", description = "租户对象")
public class Tenant extends Model<Tenant> {

    private static final long serialVersionUID = -4955897193888158062L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    /** 父主键 */
    @ApiModelProperty(value = "父主键")
    @TableField(value = "parent_id", strategy = FieldStrategy.IGNORED)
    private Long parentId;

    /** 联系人 */
    @ApiModelProperty(value = "联系人")
    private String linkman;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    /** 联系地址 */
    @ApiModelProperty(value = "联系地址")
    private String address;

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

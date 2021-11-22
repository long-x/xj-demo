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
 * @since 2019-11-22
 */
@Data
@Accessors(chain = true)
@TableName("sys_python")
@ApiModel(value = "python对象", description = "sys_python")
public class Python extends Model<Python> {

    private static final long serialVersionUID = -998723678930926471L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户名称
     */
    @ApiModelProperty(value = "Python地址")
    private String address;

    /**
     * 类型(1:默认类型)
     */
    @ApiModelProperty(value = "类型(1:默认类型)")
    private Integer type;

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
     * 通过注解方式和XML形式自定义的SQL不会自动加上逻辑条件
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

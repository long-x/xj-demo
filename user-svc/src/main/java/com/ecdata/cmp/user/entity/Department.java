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
 * @since 2019-03-21
 */
@Data
@Accessors(chain = true)
@TableName("sys_department")
@ApiModel(value = "部门对象", description = "部门对象")
public class Department extends Model<Department> {

    private static final long serialVersionUID = 28280738447780329L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 连接标识符 */
    @ApiModelProperty(value = "连接标识符")
    private String connectionId;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    /** 部门别名 */
    @ApiModelProperty(value = "部门别名")
    private String departmentAlias;

    /** 父主键 */
    @ApiModelProperty(value = "父主键")
    private Long parentId;

    /** 所有父主键字符串 */
    @ApiModelProperty(value = "所有父主键字符串(包括上级、上上级，用逗号分隔)")
    private String parentIdsStr;


    /** 排序权值 */
    @ApiModelProperty(value = "排序权值")
    private Integer score;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

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

    /**  */
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

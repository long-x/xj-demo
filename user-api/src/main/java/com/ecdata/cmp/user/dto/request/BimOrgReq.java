package com.ecdata.cmp.user.dto.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: BimOrgReq
 * @Author: shig
 * @description: 组织机构请求对象
 * @Date: 2020/3/5 8:28 下午
 */
@Data
@ApiModel(value = "竹云组织机构请求对象", description = "竹云组织机构请求对象")
public class BimOrgReq implements Serializable {
    private static final long serialVersionUID = 5096136122179483956L;
    @ApiModelProperty(value = "请求 ID")
    private String bimRequestId;

    @ApiModelProperty(value = "用户")
    private String bimRemoteUser;

    @ApiModelProperty(value = "密码")
    private String bimRemotePwd;

    @ApiModelProperty(value = "机构id")
    private String bimOrgId;


    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 连接标识符
     */
    @ApiModelProperty(value = "连接标识符")
    private String connectionId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    /**
     * 部门别名
     */
    @ApiModelProperty(value = "部门别名")
    private String departmentAlias;

    /**
     * 父主键
     */
    @ApiModelProperty(value = "父主键")
    private Long parentId;

    /**
     * 所有父主键字符串
     */
    @ApiModelProperty(value = "所有父主键字符串(包括上级、上上级，用逗号分隔)")
    private String parentIdsStr;


    /**
     * 排序权值
     */
    @ApiModelProperty(value = "排序权值")
    private Integer score;

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
     *
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
}
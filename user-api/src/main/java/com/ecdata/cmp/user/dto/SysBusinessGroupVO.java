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
 * @author ：xuj
 * @date ：Created in 2019/11/20 14:21
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组对象", description = "业务组对象")
public class SysBusinessGroupVO implements Serializable {
    private static final long serialVersionUID = 1844987411112376912L;

    /** 主键*/
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户id */
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
    @ApiModelProperty(value = "是否已删除(0未删除，1已删除)")
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

    /**
     * 关联用户组和资源池的参数
     */
    @ApiModelProperty("用户id")
    private List<String> userId;

    @ApiModelProperty("资源池id")
    private List<String> poolId;


    @ApiModelProperty(value = "用户信息")
    private List<UserVO> userList;

    @ApiModelProperty(value = "父业务组名称")
    private String parentName;

    @ApiModelProperty(value = "用户真实姓名")
    private String displayName;

    @ApiModelProperty(value = "关联资源池信息")
    private List<SysBusinessGroupResourcePoolVO> poolList;

    @ApiModelProperty(value = "子业务组")
    private List<SysBusinessGroupVO> children;



}

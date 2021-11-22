package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
@Data
@Accessors(chain = true)
@ApiModel(value = "项目对象", description = "项目对象")
public class ProjectVO implements Serializable {

    private static final long serialVersionUID = -1117137601301229752L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 集群id */
    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    /** 父id */
    @ApiModelProperty(value = "父id")
    @TableField(value = "parent_id", strategy = FieldStrategy.IGNORED)
    private Long parentId;

    /** 所有父主键字符串 */
    @ApiModelProperty(value = "所有父主键字符串(包括上级、上上级，用逗号分隔)")
    private String parentIdsStr;

    /** 项目名 */
    @ApiModelProperty(value = "项目名")
    private String projectName;

    /** 显示名称 */
    @ApiModelProperty(value = "显示名称")
    private String displayName;

    /** 关联uid */
    @ApiModelProperty(value = "关联uid")
    private String uid;

    /** 封面 */
    @ApiModelProperty(value = "封面")
    private String cover;

    /** 部门id */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

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

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已正常)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已正常)")
    private Integer isDeleted;


    /** 父项目名称 */
    @ApiModelProperty(value = "父项目名称")
    private String parentName;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

//    /** 资源池列表 */
//    @ApiModelProperty(value = "资源池列表")
//    private List<ResourcePool> resourcePoolList;

}

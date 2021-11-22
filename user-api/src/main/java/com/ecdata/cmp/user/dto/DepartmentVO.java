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
 * @author xuxinsheng
 * @since 2019-04-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "部门对象", description = "部门对象")
public class DepartmentVO implements Serializable {

    private static final long serialVersionUID = 3523923485591375306L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
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
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 是否被选中(false表示未选中,true表示已选中)
     */
    @ApiModelProperty(value = "是否被选中(false表示未选中,true表示已选中)")
    private boolean disableCheckbox;

    /** id对应前端数据树中的key */
    @ApiModelProperty(value = "id对应前端数据树中的key")
    private String key;

    /** id对应前端数据树中的value */
    @ApiModelProperty(value = "id对应前端数据树中的value")
    private String value;

    /** departmentName对应前端数据树中的title */
    @ApiModelProperty(value = "departmentName对应前端数据树中的title")
    private String title;

    /** 子部门 */
    @ApiModelProperty(value = "子部门")
    private List<DepartmentVO> children;
}

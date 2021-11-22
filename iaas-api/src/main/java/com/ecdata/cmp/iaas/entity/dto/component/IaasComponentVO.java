package com.ecdata.cmp.iaas.entity.dto.component;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:32,
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "组件对象", description = "组件对象")
public class IaasComponentVO implements Serializable {
    private static final long serialVersionUID = 4795580160989775039L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 组件种类
     */
    @ApiModelProperty(value = "组件种类")
    private String kind;

    /**
     * 显示名称
     */
    @ApiModelProperty(value = "显示名称")
    private String displayName;

//    /**
//     * 父主键
//     */
//    @ApiModelProperty(value = "父主键")
//    private Long parentId;

    /**
     * 所属操作系统
     */
    @ApiModelProperty(value = "所属操作系统(1:Windows;2:Linux;)")
    private Integer osType;


    @ApiModelProperty(value = "封面头像")
    private String cover;

    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private Integer version;

    /**
     * 初始类型
     */
    @ApiModelProperty(value = "初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)")
    private Integer initType;

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

    @ApiModelProperty(value = "用户名")
    private String uname;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已正常)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已正常)")
    private Integer isDeleted;


    @ApiModelProperty(value = "组件参数")
    private List<IaasComponentParamVO> compParams;


    @ApiModelProperty(value = "组件脚本")
    private List<IaasComponentScriptVO> compScripts;

    @ApiModelProperty(value = "组件生命周期---仅组件模块用")
    private List<IaasComponentOperationVO> compOpVOs;  //组件模块用，其它模块无视这个集合


    @ApiModelProperty(value = "key 前端的id，与系统本身无关")
    private Long key;


}

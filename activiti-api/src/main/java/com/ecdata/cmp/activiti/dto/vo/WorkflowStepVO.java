package com.ecdata.cmp.activiti.dto.vo;

import com.ecdata.cmp.common.dto.RightDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "工作流步骤对象", description = "工作流步骤对象")
public class WorkflowStepVO implements Serializable {

    private static final long serialVersionUID = -7355719919948871249L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 工作流id
     */
    @ApiModelProperty("工作流id")
    private Long workflowId;

    /**
     * 步骤名
     */
    @ApiModelProperty("步骤名")
    private String stepName;

    /**
     * 是否可修改申请(0:不可;1:可以;)
     */
    @ApiModelProperty("是否可修改申请(0:不可;1:可以;)")
    private Integer canModify;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 选项1
     */
    @ApiModelProperty("选项1")
    private String optionOne;

    /**
     * 选项2
     */
    @ApiModelProperty("选项2")
    private String optionTwo;

    /**
     * 选项3
     */
    @ApiModelProperty("选项3")
    private String optionThree;

    /**
     * 类型(1:本地流程;2:外部流程;)
     */
    @ApiModelProperty("类型(1:本地流程;2:外部流程;)")
    private Integer type;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private RightDTO rightDTO;
}

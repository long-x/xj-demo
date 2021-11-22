package com.ecdata.cmp.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-07-02
 */
@Data
@Accessors(chain = true)
@TableName("ACT_RU_TASK")
@ApiModel(value = "运行时任务节点对象", description = "运行时任务节点对象")
public class ActTaskEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    /**
     * 版次
     */
    @ApiModelProperty(value = "版次")
    @TableField(value = "REV_")
    private Integer revision;

    /**
     * 执行id
     */
    @ApiModelProperty(value = "执行id")
    @TableField(value = "EXECUTION_ID_")
    private String executionId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    @TableField(value = "PROC_INST_ID_")
    private String processInstanceId;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    @TableField(value = "PROC_DEF_ID_")
    private String processDefinitionId;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    @TableField(value = "NAME_")
    private String name;

    /**
     * 父任务id
     */
    @ApiModelProperty(value = "父任务id")
    @TableField(value = "PARENT_TASK_ID_")
    private String parentTaskId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField(value = "DESCRIPTION_")
    private String description;

    /**
     * 任务定义id
     */
    @ApiModelProperty(value = "任务定义id")
    @TableField(value = "TASK_DEF_KEY_")
    private String taskDefinitionKey;

    /**
     * 拥有人（发起人）
     */
    @ApiModelProperty(value = "拥有人（发起人）")
    @TableField(value = "OWNER_")
    private String owner;

    /**
     * 分配到任务的人
     */
    @ApiModelProperty(value = "分配到任务的人")
    @TableField(value = "ASSIGNEE_")
    private String assignee;

    /**
     * 代表团
     */
    @ApiModelProperty(value = "代表团")
    @TableField(value = "DELEGATION_")
    private String delegation;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    @TableField(value = "PRIORITY_")
    private Integer priority;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME_")
    private Date createTime;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到期时间")
    @TableField(value = "DUE_DATE_")
    private Date dueDate;

    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    @TableField(value = "CATEGORY_")
    private String category;

    /**
     * 暂停状态
     */
    @ApiModelProperty(value = "暂停状态")
    @TableField(value = "SUSPENSION_STATE_")
    private Integer suspensionState;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField(value = "TENANT_ID_")
    private String tenantId;

    /**
     * 来源key
     */
    @ApiModelProperty(value = "来源key")
    @TableField(value = "FORM_KEY_")
    private String formKey;
}

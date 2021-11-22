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
@TableName("ACT_HI_TASKINST")
@ApiModel(value = "历史任务实例对象", description = "历史任务实例对象")
public class ActHistoricTaskInstanceEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    @TableField(value = "PROC_DEF_ID_")
    private String processDefinitionId;

    /**
     * 任务定义id
     */
    @ApiModelProperty(value = "任务定义id")
    @TableField(value = "TASK_DEF_KEY_")
    private String taskDefinitionKey;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    @TableField(value = "PROC_INST_ID_")
    private String processInstanceId;

    /**
     * 执行id
     */
    @ApiModelProperty(value = "执行id")
    @TableField(value = "EXECUTION_ID_")
    private String executionId;

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
     * 拥有人
     */
    @ApiModelProperty(value = "拥有人")
    @TableField(value = "OWNER_")
    private String owner;

    /**
     * 分配到任务的人
     */
    @ApiModelProperty(value = "分配到任务的人")
    @TableField(value = "ASSIGNEE_")
    private String assignee;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    @TableField(value = "START_TIME_")
    private Date startTime;

    /**
     * 任务拾取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务拾取时间")
    @TableField(value = "CLAIM_TIME_")
    private Date claimTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    @TableField(value = "END_TIME_")
    private Date endTime;

    /**
     * 时长
     */
    @ApiModelProperty(value = "时长")
    @TableField(value = "DURATION_")
    private Long durationInMillis;

    /**
     * 从运行时任务表中删除的原因
     */
    @ApiModelProperty(value = "从运行时任务表中删除的原因")
    @TableField(value = "DELETE_REASON_")
    private String deleteReason;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    @TableField(value = "PRIORITY_")
    private Integer priority;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到期时间")
    @TableField(value = "DUE_DATE_")
    private Date dueDate;

    /**
     * 来源key
     */
    @ApiModelProperty(value = "来源key")
    @TableField(value = "FORM_KEY_")
    private String formKey;

    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    @TableField(value = "CATEGORY_")
    private String category;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField(value = "TENANT_ID_")
    private String tenantId;

}

package com.ecdata.cmp.activiti.dto.vo;

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
@ApiModel(value = "历史任务实例对象", description = "历史任务实例对象")
public class ActHistoricTaskInstanceVO implements Serializable {

    private static final long serialVersionUID = 3285088932971456719L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

    /**
     * 任务定义id
     */
    @ApiModelProperty(value = "任务定义id")
    private String taskDefinitionKey;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    /**
     * 执行id
     */
    @ApiModelProperty(value = "执行id")
    private String executionId;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String name;

    /**
     * 父任务id
     */
    @ApiModelProperty(value = "父任务id")
    private String parentTaskId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 拥有人
     */
    @ApiModelProperty(value = "拥有人")
    private String owner;

    /**
     * 分配到任务的人
     */
    @ApiModelProperty(value = "分配到任务的人")
    private String assignee;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 任务拾取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务拾取时间")
    private Date claimTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 时长
     */
    @ApiModelProperty(value = "时长")
    private Long durationInMillis;

    /**
     * 从运行时任务表中删除的原因
     */
    @ApiModelProperty(value = "从运行时任务表中删除的原因")
    private String deleteReason;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到期时间")
    private Date dueDate;

    /**
     * 来源key
     */
    @ApiModelProperty(value = "来源key")
    private String formKey;

    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    private String category;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 用作存储流程的流出步骤
     */
    @ApiModelProperty(value = "用作存储流程的流出步骤")
    private String outcome;

    /**
     * 批注信息
     */
    @ApiModelProperty(value = "批注信息")
    private String comment;

}

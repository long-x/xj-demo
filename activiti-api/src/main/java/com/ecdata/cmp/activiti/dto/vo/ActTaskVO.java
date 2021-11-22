package com.ecdata.cmp.activiti.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "运行时任务节点对象", description = "运行时任务节点对象")
public class ActTaskVO implements Serializable {

    private static final long serialVersionUID = -7483407055486054795L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 版次
     */
    @ApiModelProperty(value = "版次")
    private Integer revision;

    /**
     * 执行id
     */
    @ApiModelProperty(value = "执行id")
    private String executionId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

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
     * 任务定义id
     */
    @ApiModelProperty(value = "任务定义id")
    private String taskDefinitionKey;

    /**
     * 拥有人（发起人）
     */
    @ApiModelProperty(value = "拥有人（发起人）")
    private String owner;

    /**
     * 分配到任务的人
     */
    @ApiModelProperty(value = "分配到任务的人")
    private String assignee;

    /**
     * 代表团
     */
    @ApiModelProperty(value = "代表团")
    private String delegation;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到期时间")
    private Date dueDate;

    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    private String category;

    /**
     * 暂停状态
     */
    @ApiModelProperty(value = "暂停状态")
    private Integer suspensionState;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 来源key
     */
    @ApiModelProperty(value = "来源key")
    private String formKey;

    /**
     * 流程变量
     */
    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> variables;

    /**
     * 支持列表
     */
    @ApiModelProperty(value = "支持列表")
    private List<WorkflowTaskCandidateVO> supportList;

    /**
     * 工作流任务id
     */
    @ApiModelProperty(value = "工作流任务id")
    private Long workflowTaskId;

    /**
     * 工作流任务候选id
     */
    @ApiModelProperty(value = "工作流任务候选id")
    private Long workflowTaskCandidateId;

}

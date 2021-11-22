package com.ecdata.cmp.activiti.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "流程传输对象", description = "流程传输对象")
public class ProcessDTO implements Serializable {
    private static final long serialVersionUID = -7390283372175707136L;

    /**
     * 申请标志位(0:不发起申请;1:发起申请;)
     */
    @ApiModelProperty(value = "申请标志位(0:不发起申请;1:发起申请;)")
    private Integer applyFlag;
    /**
     * 流程定义键
     */
    @ApiModelProperty(value = "流程定义键")
    private String processDefinitionKey;
    /**
     * 自定义流程操作, 如:apply
     */
    @ApiModelProperty(value = "自定义流程操作, 如:apply")
    private String processOperation;
    /**
     * 自定义流程对象, 如:process
     */
    @ApiModelProperty(value = "自定义流程对象, 如:process")
    private String processObject;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;
    /**
     * 业务详情
     */
    @ApiModelProperty(value = "业务详情")
    private String businessDetail;
    /**
     * 流程工作流id
     */
    @ApiModelProperty(value = "流程工作流id")
    private Long processWorkflowId;

    /**
     * 发起申请时的结果
     */
    @ApiModelProperty(value = "发起申请时的结果")
    private String outcome;

    /**
     * 发起申请时的意见
     */
    @ApiModelProperty(value = "发起申请时的意见")
    private String comment;

    /**
     * 发起申请时,审批人接收到的通知消息
     */
    @ApiModelProperty(value = "发起申请时,审批人接收到的通知消息")
    private String notifyMessage;

    /**
     * 发起申请时,审批人接收到的通知详情
     */
    @ApiModelProperty(value = "发起申请时,审批人接收到的通知详情")
    private String notifyDetail;

    /**
     * 关联业务组id
     */
    @ApiModelProperty(value = "关联业务组id")
    private Long businessGroupId;

    /**
     * 关联业务组名称
     */
    @ApiModelProperty(value = "关联业务组名称")
    private String businessGroupName;

    /**
     * 自定义流程名
     */
    @ApiModelProperty(value = "自定义流程名")
    private String processName;

    /**
     * 自定义流程变量参数
     */
    @ApiModelProperty(value = "自定义流程变量参数")
    private Map<String, Object> params;

    /**
     * 用户id(调用流程开始接口时候，如果不传这个参数自动从token中获取)
     */
    @ApiModelProperty(value = "用户id(调用流程开始接口时候，如果不传这个参数自动从token中获取)")
    private Long userId;

    /**
     * 用户显示(与用户id称配套)
     */
    @ApiModelProperty(value = "用户显示名(与用户id称配套)")
    private String userDisplayName;
}

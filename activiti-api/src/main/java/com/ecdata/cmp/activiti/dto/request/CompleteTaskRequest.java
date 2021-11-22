package com.ecdata.cmp.activiti.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
public class CompleteTaskRequest implements Serializable {


    private static final long serialVersionUID = 7097822362674610924L;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 结果
     */
    @ApiModelProperty(value = "结果")
    private String outcome;

    /**
     * 意见
     */
    @ApiModelProperty(value = "意见")
    private String comment;

    /**
     * 业务详情(申请的表单信息有改动，重新设置业务详情)
     */
    @ApiModelProperty(value = "业务详情(申请的表单信息有改动，重新设置业务详情)")
    private String businessDetail;

    /**
     * 审批人接收到的通知消息
     */
    @ApiModelProperty(value = "审批人接收到的通知消息")
    private String notifyMessage;

    /**
     * 审批人接收到的通知详情
     */
    @ApiModelProperty(value = "审批人接收到的通知详情")
    private String notifyDetail;

    /**
     * 自定义流程变量参数
     */
    @ApiModelProperty(value = "自定义流程变量参数")
    private Map<String, Object> params;
    /**
     * 用户id(调动完成任务时候，如果不传这个参数，自动从token中获取)
     */
    @ApiModelProperty(value = "用户id(调动完成任务时候，如果不传这个参数，自动从token中获取)")
    private Long userId;
    /**
     * 用户显示名(与用户id称配套套)
     */
    @ApiModelProperty(value = "用户显示名(与用户id称配套)")
    private String userDisplayName;

}

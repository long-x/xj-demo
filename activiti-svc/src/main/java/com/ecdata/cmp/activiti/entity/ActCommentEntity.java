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
 * @since 2019-07-01
 */
@Data
@Accessors(chain = true)
@TableName("ACT_HI_COMMENT")
@ApiModel(value = "批注对象", description = "批注对象")
public class ActCommentEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型(event/comment)")
    @TableField(value = "TYPE_")
    private String type;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField(value = "USER_ID_")
    private String userId;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "时间")
    @TableField(value = "TIME_")
    private Date time;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    @TableField(value = "TASK_ID_")
    private String taskId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    @TableField(value = "PROC_INST_ID_")
    private String processInstanceId;

    /**
     * 操作
     */
    @ApiModelProperty(value = "操作")
    @TableField(value = "ACTION_")
    private String action;

    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    @TableField(value = "MESSAGE_")
    private String message;

    /**
     * 批注
     */
    @ApiModelProperty(value = "批注")
    @TableField(value = "FULL_MSG_")
    private String fullMessage;
}

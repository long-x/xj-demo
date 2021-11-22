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
@ApiModel(value = "批注对象", description = "批注对象")
public class ActCommentVO implements Serializable {

    private static final long serialVersionUID = -2560622022428194041L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型(event/comment)")
    private String type;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "时间")
    private Date time;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    /**
     * 操作
     */
    @ApiModelProperty(value = "操作")
    private String action;

    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String message;

    /**
     * 批注
     */
    @ApiModelProperty(value = "批注")
    private String fullMessage;
}

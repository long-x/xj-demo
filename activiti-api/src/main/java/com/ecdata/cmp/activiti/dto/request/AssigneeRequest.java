package com.ecdata.cmp.activiti.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020/4/17
 */
@Data
public class AssigneeRequest implements Serializable {

    private static final long serialVersionUID = 7391805516254389055L;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 代理人ID(若为空，则清除代理人)
     */
    @ApiModelProperty("代理人ID(若为空，则清除代理人)")
    private Long userId;

}

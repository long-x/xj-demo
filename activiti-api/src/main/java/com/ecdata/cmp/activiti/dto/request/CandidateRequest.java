package com.ecdata.cmp.activiti.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
public class CandidateRequest implements Serializable {

    private static final long serialVersionUID = 3431287265760788711L;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 候选ID
     */
    @ApiModelProperty("候选ID")
    private Long candidateId;

    /**
     * 类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)
     */
    @ApiModelProperty("类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)")
    private Integer type;

}

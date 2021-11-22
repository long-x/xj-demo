package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:10
 */
@Data
public class ProgramSupportParam {
    @ApiModelProperty(value = "申请id")
    private Long applyId;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty("候选ID")
    private Long candidateId;

    @ApiModelProperty("类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)")
    private Integer type;
}

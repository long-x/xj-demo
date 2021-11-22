package com.ecdata.cmp.iaas.entity.dto.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/5 14:30
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "获取部门", description = "获取部门")
public class DeptCountVO {


    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("业务组名称")
    private String businessGroupName;
}

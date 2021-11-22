package com.ecdata.cmp.iaas.entity.dto.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 云平台资源统计报表
 *
 * @author xuxiaojian
 * @date 2020/5/11 10:50
 */
@Data
public class CloudResourceAssignedStatisticsVO {
    private Long id;
    @ApiModelProperty("二级部门名称")
    private String name;

    @ApiModelProperty("三级部门")
    private List<ThreeDepartment> children;
}

package com.ecdata.cmp.iaas.entity.dto.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/12 13:48
 */
@Data
public class ThreeDepartment {
    private Long id;
    @ApiModelProperty("三级部门名称")
    private String threeName;

    @ApiModelProperty("系统数量")
    private int systemNum;

    @ApiModelProperty("分区")
    private List<AreaInfoVO> children;

}

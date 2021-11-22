package com.ecdata.cmp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
*/
@Data
@Accessors(chain = true)
@ApiModel(value = "权限数据传输对象", description = "权限数据传输对象")
public class RightDTO implements Serializable {

    private static final long serialVersionUID = 2339110423471047645L;
    /**
     * 对象id
     */
    @ApiModelProperty(value = "对象id")
    private Long id;

    /**
     * 用户id列表
     */
    @ApiModelProperty(value = "用户id列表")
    private List<Long> userIdList;

    /**
     * 角色id列表
     */
    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIdList;

    /**
     * 部门id列表
     */
    @ApiModelProperty(value = "部门id列表")
    private List<Long> departmentIdList;

    /**
     * 项目id列表(废弃)
     */
    @ApiModelProperty(value = "项目id列表(废弃)")
    private List<Long> projectIdList;

    /**
     * 业务组id列表
     */
    @ApiModelProperty(value = "业务组id列表")
    private List<Long> businessGroupIdList;
}

package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/22 18:34
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组关联用户对象", description = "业务组关联用户对象")
public class SysBusinessMemberAndUserAndPoolVO {

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("用户id")
    private List<String> userId;

    @ApiModelProperty("资源池id")
    private List<String> poolId;

    @ApiModelProperty("部门id")
    private List<String> departmentId;

    @ApiModelProperty(value = "关联资源池信息")
    private List<SysBusinessGroupResourcePoolVO> poolList;

}

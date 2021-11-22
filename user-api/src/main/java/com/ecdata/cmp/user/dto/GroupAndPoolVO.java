package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 14:35
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组关联关系对象", description = "业务组关联关系对象")
public class GroupAndPoolVO implements Serializable {
    private static final long serialVersionUID = 6917421524561665210L;


    @ApiModelProperty(value = "业务组名")
    private String businessGroupName;


    @ApiModelProperty("资源池id")
    private Long poolId;


}

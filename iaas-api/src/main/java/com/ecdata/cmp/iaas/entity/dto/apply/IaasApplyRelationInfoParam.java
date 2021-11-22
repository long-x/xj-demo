package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:10
 */
@Data
public class IaasApplyRelationInfoParam {
    @ApiModelProperty(value = "关联用户")
    private Long relationUser;
    @ApiModelProperty(value = "关联用户名称")
    private String relationUserName;
    @ApiModelProperty(value = "关联关系")
    private String relationInfo;

}

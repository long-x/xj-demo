package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxiaojian
 * @date 2020/4/14 14:33
 */
@Data
public class ApplyAreaUpdateVO {
    @ApiModelProperty(value = "配置id")
    private Long configId;

    @ApiModelProperty(value = "区域id")
    private String areaId;

    @ApiModelProperty(value = "是否自动发放资源")
    private String autoIssue;

    @ApiModelProperty("虚拟机ip")
    private String ipAddress;

    @ApiModelProperty("虚拟机eip")
    private String eipId;
}

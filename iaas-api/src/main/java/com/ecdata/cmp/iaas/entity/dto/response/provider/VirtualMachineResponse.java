package com.ecdata.cmp.iaas.entity.dto.response.provider;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 虚拟机
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualMachineResponse {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("虚机名")
    private String vmName;

    @ApiModelProperty(value = "0:未知;1:创建中;2:开机中;3:开机;4:挂起中;5:挂起;6:关机中;7:关机")
    private Integer status;

    @ApiModelProperty("操作系统全名")
    private String osName;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("操作系统类型(1:windows;2:linux;)")
    private Integer osType;
}

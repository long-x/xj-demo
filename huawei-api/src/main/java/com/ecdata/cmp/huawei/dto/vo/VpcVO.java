package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/26 21:52
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟私有云VPC列表", description = "虚拟私有云VPC列表")
public class VpcVO {


    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "子网id")
    private String network_id;

    @ApiModelProperty(value = "enable_snat")
    private String enable_snat;

    @ApiModelProperty(value = "expiry")
    private String expiry;

    @ApiModelProperty(value = "op_status")
    private String op_status;

    @ApiModelProperty(value = "项目id")
    private String project_id;

    @ApiModelProperty(value = "ntp_v6")
    private String ntp_v6;



}

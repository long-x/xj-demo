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
 * @date ：Created in 2020/10/19 16:03
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "弹性IP对象", description = "弹性IP对象")
public class EipVO {


    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "弹性IP类型(生产/测试)")
    private String type;

    @ApiModelProperty(value = "external_net_id")
    private String external_net_id;

    @ApiModelProperty(value = "弹性IP地址")
    private String public_ip_address;

    @ApiModelProperty(value = "弹性IP关联的私网IP地址")
    private String private_ip_address;

    @ApiModelProperty(value = "弹性IP状态(只能使用状态为DOWN的未绑定)")
    private String status;

    @ApiModelProperty(value = "租户ID")
    private String tenant_id;

    @ApiModelProperty(value = "项目ID")
    private String project_id;


}

package com.ecdata.cmp.iaas.entity.dto.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请虚机网络表", description = "iaas流程申请虚机网络表")
public class IaasProcessApplyVirtualMachineNetworkVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("流程申请虚机id")
    private Long vmId;

    @ApiModelProperty("集群网络id")
    private Long networkId;

    @ApiModelProperty("网段id")
    private Long segmentId;

    @ApiModelProperty("网关")
    private String gateway;

    @ApiModelProperty("ip地址")
    private String ipAddress;

    @ApiModelProperty("dns")
    private String dns;

    @ApiModelProperty("cidr")
    private String cidr;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Boolean isDeleted;

}

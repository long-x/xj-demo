package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * iaas虚机磁盘表
 *
 * @author ：xxj
 * @date ：Created in 2019/11/25 18:24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas虚机磁盘表", description = "iaas虚机磁盘表")
public class IaasVirtualMachineDiskVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群id")
    private Long vmId;

    @ApiModelProperty("'关联唯一key'")
    private String diskKey;

    @ApiModelProperty("'虚机名'")
    private String diskName;

    @ApiModelProperty("磁盘状态")
    private String status;

    @ApiModelProperty("磁盘类型")
    private String diskType;

    @ApiModelProperty("总磁盘大小(GB)")
    private Double diskTotal;

    @ApiModelProperty("已用磁盘大小(GB)")
    private Double diskUsed;

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
    private boolean isDeleted;

}

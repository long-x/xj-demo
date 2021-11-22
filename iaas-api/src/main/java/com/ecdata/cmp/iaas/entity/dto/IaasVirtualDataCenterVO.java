package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "iaas虚拟数据中心表", description = "iaas虚拟数据中心表")
public class IaasVirtualDataCenterVO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群id")
    private Long clusterId;

    @ApiModelProperty("供应商id")
    private Long providerId;

    @ApiModelProperty("供应商id")
    private String projectKey;

    @ApiModelProperty("虚拟数据中心名")
    private String vdcName;

    @ApiModelProperty("vdc域名称")
    private String domainName;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("关联远程用户key")
    private String userKey;

    @ApiModelProperty("关联唯一key")
    private String vdcKey;

    @ApiModelProperty("排序")
    private Integer score;

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

    @ApiModelProperty("项目")
    private List<IaasProjectVO> children;
}

package com.ecdata.cmp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @title: SysBusinessGroupResourcePool
 * @Author: shig
 * @description: 业务资源池拓展类
 * @Date: 2019/11/24 12:52 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务资源拓展类对象", description = "业务资源拓展类对象")
public class SysBusinessGroupResourcePoolVO implements Serializable {

    private static final long serialVersionUID = -6314090512463337457L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("类型(1:iaas;2:paas;)")
    private Integer type;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("资源池id")
    private Long poolId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("业务组ids(多个逗号分隔)")
    private String businessIds;
    @ApiModelProperty("业务组名称")
    private String businessGroupName;

    @ApiModelProperty("资源池名称")
    private String poolName;

    @ApiModelProperty("资源池类型/iaas 1:vCenter;2:华为云 /paas 1:k8s集群;2:openshift集群")
    private String providerType;

    @ApiModelProperty("供应商名称")
    private String providerName;

    @ApiModelProperty(value = "关联资源池信息")
    private List<SysBusinessGroupResourcePoolVO> poolList;

}

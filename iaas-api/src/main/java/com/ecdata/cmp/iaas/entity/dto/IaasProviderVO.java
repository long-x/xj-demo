package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @title: iaasProvider vo
 * @Author: shig
 * @description: 供应商拓展类
 * @Date: 2019/11/12 9:40 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "供应商拓展类 对象", description = "供应商拓展类 对象")
public class IaasProviderVO implements Serializable {

    private static final long serialVersionUID = -8703650666762791793L;

    /*iaasProvider属性*/
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 供应商名
     */
    @ApiModelProperty(value = "供应商名")
    private String providerName;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private Long type;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * vdc domainName
     */
    @ApiModelProperty(value = "vdc domainName")
    private String domainName;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 端口
     */
    @ApiModelProperty(value = "端口")
    private String port;

    /**
     * 运营面地址
     */
    @ApiModelProperty(value = "运营面地址")
    private String authAddress;

    /**
     * auth用户名
     */
    @ApiModelProperty(value = "auth用户名")
    private String authUsername;

    /**
     * auth密码
     */
    @ApiModelProperty(value = "auth密码")
    private String authPassword;

    /**
     * 运维面接口调用地址
     */
    @ApiModelProperty(value = "运维面调用地址")
    private String ocAddress;

    /**
     * oc用户名
     */
    @ApiModelProperty(value = "oc用户名")
    private String ocUsername;

    /**
     * oc密码
     */
    @ApiModelProperty(value = "oc密码")
    private String ocPassword;

    /**
     * 弹性云服务调用地址
     */
    @ApiModelProperty(value = "弹性云服务调用地址")
    private String ecsAddress;

    /**
     * ecs用户名
     */
    @ApiModelProperty(value = "ecs用户名")
    private String ecsUsername;

    /**
     * oc密码
     */
    @ApiModelProperty(value = "ecs密码")
    private String ecsPassword;

    /**
     * 云硬盘调用地址
     */
    @ApiModelProperty(value = "云硬盘调用地址")
    private String evsAddress;


    /**
     * evs用户名
     */
    @ApiModelProperty(value = "evs用户名")
    private String evsUsername;

    /**
     * evs密码
     */
    @ApiModelProperty(value = "evs密码")
    private String evsPassword;

    /**
     * 虚拟私有云调用地址
     */
    @ApiModelProperty(value = "虚拟私有云调用地址")
    private String vpcAddress;

    /**
     * vpc用户名
     */
    @ApiModelProperty(value = "vpc用户名")
    private String vpcUsername;

    /**
     * evs密码
     */
    @ApiModelProperty(value = "vpc密码")
    private String vpcPassword;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已正常)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    //其他对象属性
    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    @ApiModelProperty(value = "vdc list")
    private List<IaasVirtualDataCenterVO> virtualDataCenterVOS;

    @ApiModelProperty(value = "项目list")
    private List<IaasProjectVO> projectVOS;


    /**
     * 运维面调用地址
     */
    @ApiModelProperty(value = "运维面调用地址")
    private String ocInterfaceAddress;

    /**
     * oc接口用户名
     */
    @ApiModelProperty(value = "oc接口用户名")
    private String ocInterfaceUsername;

    /**
     * oc接口密码
     */
    @ApiModelProperty(value = "oc接口密码")
    private String ocInterfacePassword;

    /**
     * 裸金属服务地址
     */
    @ApiModelProperty(value = "裸金属服务地址")
    private String bmsAddress;

    /**
     * 镜像服务地址
     */
    @ApiModelProperty(value = "镜像服务地址")
    private String imsAddress;



}
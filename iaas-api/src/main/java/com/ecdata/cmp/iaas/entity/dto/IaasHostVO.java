package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: IaasHostVO
 * @Author: shig
 * @description: 主机对象
 * @Date: 2019/11/23 1:25 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "主机拓展 对象", description = "主机拓展 对象")
public class IaasHostVO implements Serializable {

    private static final long serialVersionUID = 5632357185259488810L;

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
     * 主机名
     */
    @ApiModelProperty(value = "主机名")
    private String hostName;

    /**
     * 类型
     */
    @ApiModelProperty(value = "华为：1，vcenter:2")
    private Integer type;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态：华为专用")
    private String state;

    /**
     * 状态
     */
    @ApiModelProperty(value = "0:未知;1:创建中;2:开机中;3:开机;4:挂起中;5:挂起;6:关机中;7:关机")
    private Integer status;

    /**
     * 集群id
     */
    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    /**
     * CPU总量(MHz)
     */
    @ApiModelProperty(value = " CPU总量(MHz)")
    private Integer cpuTotal;

    /**
     * CPU使用(MHz)
     */
    @ApiModelProperty(value = "CPU使用(MHz)")
    private Integer cpuUsed;

    /**
     * 内存总量(MB)
     */
    @ApiModelProperty(value = "内存总量(MB)")
    private Integer memoryTotal;

    /**
     * 内存使用(MB)
     */
    @ApiModelProperty(value = "内存使用(MB)")
    private Integer memoryUsed;

    /**
     * 磁盘总量(GB)
     */
    @ApiModelProperty(value = " 磁盘总量(GB)")
    private Integer diskTotal;

    /**
     * 磁盘使用(GB)
     */
    @ApiModelProperty(value = "磁盘使用(GB)")
    private Integer diskUsed;

    /**
     * 虚拟CPU核数
     */
    @ApiModelProperty(value = "虚拟CPU核数")
    private Integer vcpu;

    /**
     * 数据中心
     */
    @ApiModelProperty(value = "数据中心")
    private String dataCenter;

    /**
     * EXSI Build号
     */
    @ApiModelProperty(value = "EXSI Build号")
    private String exsiBuildNumber;

    /**
     * EXSI详细版本
     */
    @ApiModelProperty(value = "EXSI详细版本")
    private String exsiFullVersion;

    /**
     * EXSI版本
     */
    @ApiModelProperty(value = "EXSI版本")
    private String exsiVersion;

    /**
     * 主机代理API版本
     */
    @ApiModelProperty(value = "主机代理API版本")
    private String hostAgentApiVersion;

    /**
     * 逻辑处理器
     */
    @ApiModelProperty(value = "逻辑处理器")
    private Integer logicProcessor;

    /**
     * MoRef ID
     */
    @ApiModelProperty(value = "MoRef ID")
    private String morefId;

    /**
     * 虚拟网卡
     */
    @ApiModelProperty(value = "虚拟网卡")
    private Integer nics;

    /**
     * 逻辑处理器
     */
    @ApiModelProperty(value = "逻辑处理器")
    private Integer processorSocket;

    /**
     * 启用VNC
     */
    @ApiModelProperty(value = "启用VNC")
    private Boolean enabled;

    /**
     * 起始端口
     */
    @ApiModelProperty(value = "起始端口")
    private Integer fromPort;

    /**
     * 结束端口
     */
    @ApiModelProperty(value = "结束端口")
    private Integer toPort;

    /**
     * 代理地址
     */
    @ApiModelProperty(value = "代理地址")
    private String proxyAddress;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "关联唯一key")
    private String hostKey;

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
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已正常)
     *
     * @TableLogic:removeById逻辑删除调用，pkVal()也要有
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    @ApiModelProperty("总磁盘大小GB")
    private String spaceTotal;

    @ApiModelProperty("已用磁盘大小GB")
    private String spaceUsed;

    @ApiModelProperty("集群名称")
    private String clusterName;

    @ApiModelProperty("实例数")
    private String instanceNumber;

    /**
     * 其他属性
     */
    @ApiModelProperty("CPU超分比")
    private String cpuRadio;

    @ApiModelProperty("内存超分比")
    private String memoryRadio;

    @ApiModelProperty(value = "resId")
    private String resId;
}
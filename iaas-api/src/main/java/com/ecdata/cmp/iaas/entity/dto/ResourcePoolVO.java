package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @title: ResourcePoolVO
 * @Author: shig
 * @description: 资源池 拓展类对象
 * @Date: 2019/11/18 9:40 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源池 拓展类对象", description = "资源池 拓展类对象")
public class ResourcePoolVO implements Serializable {
    private static final long serialVersionUID = 6504050609893818325L;

    @ApiModelProperty(value = "类型：1提交，2提交并同步")
    private String type;
    /*ResourcePool属性*/
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    /**
     * vcd id
     */
    @ApiModelProperty(value = "vcd id")
    private Long vdcId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 集群id
     */
    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    /**
     * 资源池名
     */
    @ApiModelProperty(value = "资源池名")
    private String poolName;

    /**
     * 标签(逗号分隔)
     */
    @ApiModelProperty(value = "标签(逗号分隔)")
    private String tag;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    /**
     * 分配的虚拟cpu数量
     */
    @ApiModelProperty(value = "分配的虚拟cpu数量")
    private Integer vcpuTotalAllocate;

    /**
     * 已使用分配的虚拟cpu数量
     */
    @ApiModelProperty(value = "已使用分配的虚拟cpu数量")
    private Double vcpuUsedAllocate;

    /**
     * 分配的内存大小(mb)
     */
    @ApiModelProperty(value = "分配的内存大小(mb)")
    private Integer memoryTotalAllocate;

    /**
     * 已使用分配的内存大小(mb)
     */
    @ApiModelProperty(value = "已使用分配的内存大小(mb)")
    private Double memoryUsedAllocate;

    /**
     * 分配的虚拟机数量
     */
    @ApiModelProperty(value = "分配的虚拟机数量")
    private Integer vmTotalAllocate;

    /**
     * 已使用分配的虚拟机数量
     */
    @ApiModelProperty(value = "已使用分配的虚拟机数量")
    private Integer vmUsedAllocate;

    /**
     * 分配的pod数量
     */
    @ApiModelProperty(value = "分配的pod数量")
    private Integer podTotalAllocate;

    /**
     * 已使用分配的pod数量
     */
    @ApiModelProperty(value = "已使用分配的pod数量")
    private Integer podUsedAllocate;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除，1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除，1表示已删除)")
    private Boolean isDeleted;


    //资源池 主分页列表属性
    @ApiModelProperty(value = "供应商类型")
    private String providerType;

    //供应商名称
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @ApiModelProperty(value = "业务组id(多个逗号分隔)")
    private String businessIds;

    @ApiModelProperty(value = "业务组名称(多个逗号分隔)")
    private String businessNames;

    //用户名
    @ApiModelProperty(value = "用户名")
    private String displayName;

    //物理CPU核心总数量:sum(iaas_host.cpu_total)
    @ApiModelProperty(value = "物理CPU核心总数量")
    private String cpuTotal;

    //已分配cCPU数量(超分比)=iaas_resource_pool.vcpu_total_allocate/sum(iaas_host.cpu_total)
    @ApiModelProperty(value = " 已分配vCPU数量(超分比)")
    private String cpuTTHostRatio;

    //物理内存总数量:sum(iaas_host.memory_total)
    @ApiModelProperty(value = "物理内存总数量")
    private String memoryTotal;

    //已分配内存总数量(超分比)=iaas_resource_pool.memory_total_allocate/sum(iaas_host.memory_total)
    @ApiModelProperty(value = "已分配内存总数量(超分比)")
    private String vmTTHostRatio;

    //已分配存储（超分比）=memory_total_allocate/memory_used_allocate
    @ApiModelProperty(value = " 已分配存储（超分比）")
    private String memoryTURatio;

    //分配的虚拟机数量（超分比）=vm_total_allocate/vm_used_allocate
    @ApiModelProperty(value = "分配的虚拟机数量（超分比）")
    private String vmTURratio;


    @ApiModelProperty(value = "项目projectKey")
    private String projectKey;

    @ApiModelProperty(value = "vdc域名称")
    private String domainName;

    @ApiModelProperty(value = "vdc用户名")
    private String username;

    @ApiModelProperty(value = "vdc密码")
    private String password;

    @ApiModelProperty(value = "vmKeyList")
    private List<String> vmKeyList;

    @ApiModelProperty(value = "预留的分配的虚拟cpu数量")
    private Integer vcpuReservedAllocate;

    @ApiModelProperty(value = "预留的分配的内存大小(MB)")
    private Integer memoryReservedAllocate;

    @ApiModelProperty(value = "预留的分配的虚拟机数量")
    private Integer vmReservedAllocate;

    @ApiModelProperty(value = "预留的分配的pod数量")
    private Integer podReservedAllocate;

    @ApiModelProperty(value = "0 纳管虚拟机  1  纳管裸金属")
    private Integer vmOrMetal;

}
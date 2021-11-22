package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @create 2019-11-13 13:49
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源池表对象", description = "资源池表对象")
public class IaasResourcePoolVO {
    private static final long serialVersionUID = -5356687720974317119L;
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
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

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

    @ApiModelProperty(value = "预留的分配的虚拟cpu数量")
    private Integer vcpuReservedAllocate;

    @ApiModelProperty(value = "预留的分配的内存大小(MB)")
    private Integer memoryReservedAllocate;

    @ApiModelProperty(value = "预留的分配的虚拟机数量")
    private Integer vmReservedAllocate;

    @ApiModelProperty(value = "预留的分配的pod数量")
    private Integer podReservedAllocate;


    /**
     * 是否已删除(0表示未删除,1表示已正常)
     *
     * @TableLogic:removeById逻辑删除调用，pkVal()也要有
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

}

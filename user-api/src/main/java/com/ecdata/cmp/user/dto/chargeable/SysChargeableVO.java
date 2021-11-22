package com.ecdata.cmp.user.dto.chargeable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/27 12:52,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "计费模型", description = "计费模型")
public class SysChargeableVO implements Serializable {
    private static final long serialVersionUID = 3219790282120838866L;

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
     * 区域id
     */
    @ApiModelProperty(value = "非空 区域id")
    private Long areaId;

    /**
     * 集群id
     */
    @ApiModelProperty(value = "非空 集群id")
    private Long clusterId;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "非空 供应商id")
    private Long providerId;

    /**
     * 资源池ID
     */
    @ApiModelProperty(value = "非空 资源池ID")
    private Long poolId;

    /**
     * 类型(1:iaas;2:paas;)
     */
    @ApiModelProperty(value = "类型(1:iaas;2:paas;)")
    private Integer poolType;

    /**
     * 计费类型
     */
    @ApiModelProperty(value = "非空 计费类型")
    private String chargingType;

    /**
     * 货币单位
     */
    @ApiModelProperty(value = "货币单位")
    private String monetaryUnit;

    /**
     * 周期(0:永久;1:小时;2:天;3:周;4:月;5:年;)
     */
    @ApiModelProperty(value = "周期(0:永久;1:小时;2:天;3:周;4:月;5:年;)")
    private Integer period;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;

    /**
     * 类型(1:CPU;2:Memory;3:Storage;4:FloatingIP;5:LoadBalancer;6:LoadBalancerListener;)
     */
    @ApiModelProperty(value = "非空 类型(1:CPU;2:Memory;3:Storage;4:FloatingIP;5:LoadBalancer;6:LoadBalancerListener;)")
    private Integer type;

    /**
     * 辅助类型
     */
    @ApiModelProperty(value = "辅助类型")
    private String subType;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 规格ID
     */
    @ApiModelProperty(value = "规格ID")
    private Long flavorId;

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
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    @ApiModelProperty(value = "供应商id")
    private Long proId;

    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @ApiModelProperty(value = "资源池id")
    private Long plId;

    @ApiModelProperty(value = "资源池名称")
    private String poolName;

    @ApiModelProperty(value = "资源池对应供应商Id")
    private Long rpProId;

    @ApiModelProperty(value = "地区id")
    private Long  arAreaId;

    @ApiModelProperty(value = "地区名称")
    private String  areaName;

    @ApiModelProperty(value = "集群id")
    private Long clClustId;

    @ApiModelProperty(value = "集群名称")
    private String clusterName;
}

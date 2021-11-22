package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  zhaoyx
 * @Date 2019-11-27 
 */

@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_chargeable")
@ApiModel(value = "计费模型", description = "计费模型")
public class SysChargeable  extends Model<SysChargeable> {

	private static final long serialVersionUID =  3940533948040150840L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.INPUT)
	private Long id;

	/**
	 * 租户id
	 */
  @ApiModelProperty(value = "租户id")
	private Long tenantId;

	/**
	 * 区域id
	 */
	@ApiModelProperty(value = "区域id")
	private Long areaId;

	/**
	 * 集群id
	 */
	@ApiModelProperty(value = "集群id")
	private Long clusterId;

	/**
	 * 供应商id
	 */
  @ApiModelProperty(value = "供应商id")
	private Long providerId;

	/**
	 * 资源池ID
	 */
  @ApiModelProperty(value = "资源池ID")
	private Long poolId;

	/**
	 * 类型(1:iaas;2:paas;)
	 */
	@ApiModelProperty(value = "类型(1:iaas;2:paas;)")
	private Integer poolType;

	/**
	 * 计费类型
	 */
  @ApiModelProperty(value = "计费类型")
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
  @ApiModelProperty(value = "类型(1:CPU;2:Memory;3:Storage;4:FloatingIP;5:LoadBalancer;6:LoadBalancerListener;)")
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
	@TableLogic
  	@ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
	private Integer isDeleted;

	/** 指定主键 */
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

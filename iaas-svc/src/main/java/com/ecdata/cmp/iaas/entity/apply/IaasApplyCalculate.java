package com.ecdata.cmp.iaas.entity.apply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 计算资源表
 * 
 * @author xxj
 * @date 2020-03-10 13:50:16
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_apply_calculate")
@ApiModel(value = "计算资源表", description = "计算资源表")
public class IaasApplyCalculate extends Model<IaasApplyCalculate> {

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
	 * 申请id
	 */
	@ApiModelProperty(value = "申请id")
	private Long applyId;

	/**
	 * 配置id
	 */
	@ApiModelProperty(value = "配置id")
	private Long configId;

	/**
	 * 所在区域
	 */
	@ApiModelProperty(value = "所在区域")
	private Long areaId;
	/**
	 * 操作类型：1.新增；2.变更;3.删除
	 */
	@ApiModelProperty(value = "操作类型：1.新增；2.变更;3.删除")
	private String operationType;
	/**
	 * 计算资源类型：1.CPU 2.内存
	 */
	@ApiModelProperty(value = "计算资源类型：1.CPU 2.内存")
	private String resourceType;
	/**
	 * 数量（G）
	 */
	@ApiModelProperty(value = "数量（G）")
	private Integer resourceNum;

	@ApiModelProperty(value = "型号")
	private String model;

	@ApiModelProperty(value = "cpu大小")
	private Integer cpu;

	@ApiModelProperty(value = "内存大小")
	private Integer memory;

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
	 * 是否已删除(0表示未删除,1表示已删除)
	 */
	@ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
	private Integer isDeleted;

	/**
	 * 指定主键
	 */
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

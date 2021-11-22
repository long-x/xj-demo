package com.ecdata.cmp.iaas.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  zhaoyx
 * @Date 2019-12-06 
 */

@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_alert")
@ApiModel(value = "告警对象", description = "告警对象")
public class IaasAlert  extends Model<IaasAlert> {

	private static final long serialVersionUID =  7692643801322777344L;

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
	 * 集群ID
	 */
  @ApiModelProperty(value = "集群ID")
	private Long clusterId;

	/**
	 * 资源名(触发于)
	 */
  @ApiModelProperty(value = "资源名(触发于)")
	private String resourceName;

	/**
	 * 警示级别
	 */
  @ApiModelProperty(value = "警示级别")
	private String alertLevel;

	/**
	 * 警示定义名(警示)
	 */
  @ApiModelProperty(value = "警示定义名(警示)")
	private String alertDefinitionName;

	/**
	 * 状态
	 */
  @ApiModelProperty(value = "状态")
	private String status;

	/**
	 * 警示类型
	 */
  @ApiModelProperty(value = "警示类型")
        private String type;

	/**
	 * 警示子类型
	 */
  @ApiModelProperty(value = "警示子类型")
	private String subType;

	/**
	 * 开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "开始时间")
	private Date startTime;

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
	 * 是否已删除(0表示未删除,1表示已删除)
	 */
  @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
	private Integer isDeleted;


	/** 指定主键 */
	@Override
	protected Serializable pkVal() {
		return this.id;
	}


	@ApiModelProperty(value = "华为告警id")
	private Long csn;

	@ApiModelProperty(value = "1:未处理;2:已处理;3:处理中;")
	private Integer visible;







	@ApiModelProperty("告警级别")
	private Integer severity;

	@ApiModelProperty("告警名称")
	private String alarmName;

	@ApiModelProperty("告警源")
	private String meName;

	@ApiModelProperty("定位信息")
	private String moi;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("最近发生时间")
	private Date latestOccurTime;   //long

	@ApiModelProperty("告警区域")
	private String logicalRegionName;

	@ApiModelProperty("告警附加信息")
	private String additionalInformation;

	@ApiModelProperty("告警id")
	private String alarmId;

	@ApiModelProperty("告警规则名称")
	private String ruleName;

	@ApiModelProperty("告警信息地址")
	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("第一次发生时间")
	private Date firstOccurTime;     //long

	@ApiModelProperty("告警区域id")
	@TableField(value = "logical_region_id")
	private String logicalRegionId;

	@ApiModelProperty("vdcid")
	private String vdcId;

	@ApiModelProperty("vdc名称")
	private String vdcName;

	@ApiModelProperty("originsystem")
	private String originSystem;

	@ApiModelProperty("originsystem名称")
	private String originSystemName;

}

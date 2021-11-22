package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("temp_huawei_cloud_security_cloud_alert_condition")
@ApiModel(value = " 华为云及安全云告警情况临时表 对象", description = " 华为云及安全云告警情况临时表 对象")
public class TempHuaweiCloudSecurityCloudAlertCondition extends Model<TempHuaweiCloudSecurityCloudAlertCondition> {
    private static final long serialVersionUID = 2054921514210357007L;
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
     * 类型（华为云、安全云）
     */
    @ApiModelProperty(value = "类型（华为云、安全云）")
    private String type;

    /**
     * 告警数
     */
    @ApiModelProperty(value = "告警数")
    private Integer alertNum;

    /**
     * 已处置数
     */
    @ApiModelProperty(value = "已处置数")
    private Integer disposeNum;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    @ApiModelProperty(value = "警示级别(1.紧急 2.重要 3.次要 4.提示)")
    private Boolean alertLevel;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
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

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 10:37
 * @modified By：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_accounting_rules")
@ApiModel(value = "计量计费", description = "计量计费")
public class IaasAccountingRules extends Model<IaasAccountingRules> {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectiveDate;

    @ApiModelProperty("失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expirationDate;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("状态")
    private int status;

    @ApiModelProperty("cpu单价")
    private Long cpuUnitPrice;

    @ApiModelProperty("内存单价")
    private Long memoryUnitPrice;

    @ApiModelProperty("磁盘单价")
    private Long diskUnitPrice;

    @ApiModelProperty("裸金属型号1单价")
    private Long bmsType1UnitPrice;

    @ApiModelProperty("裸金属型号2单价")
    private Long bmsType2UnitPrice;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}

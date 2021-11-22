package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/15 14:13
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "计量计费统计对象", description = "计量计费统计对象")
public class IaasAccountingStatisticsVO extends Model<IaasAccountingStatisticsVO> {


    private static final long serialVersionUID = 339820868373303412L;


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("组织Id")
    private Long orgId;

    @ApiModelProperty("组织名称")
    private String orgName;

    @ApiModelProperty("生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectiveDate;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("状态")
    private int status;

    @ApiModelProperty("cpu数量")
    private Long cpuCount;

    @ApiModelProperty("cpu单价")
    private Long cpuUnitPrice;

    @ApiModelProperty("内存数量")
    private Long memoryCount;

    @ApiModelProperty("内存单价")
    private Long memoryUnitPrice;

    @ApiModelProperty("磁盘数量")
    private Long diskCount;

    @ApiModelProperty("磁盘单价")
    private Long diskUnitPrice;

    @ApiModelProperty("裸金属型号1数量")
    private Long bmsType1Count;

    @ApiModelProperty("裸金属型号1单价")
    private Long bmsType1UnitPrice;

    @ApiModelProperty("裸金属型号2数量")
    private Long bmsType2Count;

    @ApiModelProperty("裸金属型号2单价")
    private Long bmsType2UnitPrice;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;

    @ApiModelProperty("合计")
    private Long money;



}

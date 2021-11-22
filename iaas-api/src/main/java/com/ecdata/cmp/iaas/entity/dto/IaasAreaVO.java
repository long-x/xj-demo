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
 * @create 2019-11-13 9:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "区域对象", description = "区域对象")
public class IaasAreaVO {
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
     * 区域名
     */
    @ApiModelProperty(value = "区域名")
    private String areaName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer score;

    /**
     * 是否默认(0表示非默认区域，1表示是默认区域)
     */
    @ApiModelProperty(value = "是否默认(0表示非默认区域，1表示是默认区域)")
    private Boolean isDefault;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "关联唯一key")
    private String areaKey;

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
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已正常)")
    private Boolean isDeleted;
}

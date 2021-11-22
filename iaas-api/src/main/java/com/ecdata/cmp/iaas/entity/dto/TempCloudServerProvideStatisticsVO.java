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

/**
 * @title: TempCloudServerProvideStatistics
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 2:55 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "云服务发放统计临时表 拓展类对象", description = "云服务发放统计临时表 拓展类对象")
public class TempCloudServerProvideStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1821261592763411124L;
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
     * '统计名称'
     */
    @ApiModelProperty(value = "'统计名称'")
    private String dataName;

    /**
     * '对应的统计数量'
     */
    @ApiModelProperty(value = "'对应的统计数量'")
    private Integer dataNum;

    /**
     * '数据类型:1:云资源发放统计,2:安全类资源发放统计'
     */
    @ApiModelProperty(value = "'数据类型:1:云资源发放统计,2:安全类资源发放统计'")
    private Integer dataType;

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
    private Boolean isDeleted;
}
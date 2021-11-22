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


@Data
@Accessors(chain = true)
@ApiModel(value = "安全云安全资源一览临时表 拓展类对象", description = "安全云安全资源一览临时表 拓展类对象")
public class TempSecurityCloudSecurityResourceOverviewVO implements Serializable {
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
     * 防护天数
     */
    @ApiModelProperty(value = "防护天数")
    private Integer protectionDayNum;

    /**
     * 用户总数
     */
    @ApiModelProperty(value = "用户总数")
    private Integer userNum;

    /**
     * 业务总数
     */
    @ApiModelProperty(value = "业务总数")
    private Integer businessNum;

    /**
     * 安全事件业务数
     */
    @ApiModelProperty(value = "安全事件业务数")
    private Integer securityEventBusinessNum;

    /**
     * 达到二级合规数
     */
    @ApiModelProperty(value = "达到二级合规数")
    private Integer levelTwoComplianceNum;

    /**
     * 满足三级合规数
     */
    @ApiModelProperty(value = "满足三级合规数")
    private Integer levelThreeComplianceNum;

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

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

}
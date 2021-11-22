package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @create 2019-11-13 13:38
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "主机存储对象", description = "主机存储对象")
public class IaasHostDatastoreVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 主机id
     */
    @ApiModelProperty(value = "主机id")
    private Long hostId;

    /**
     * 存储名
     */
    @ApiModelProperty(value = "存储名")
    private String datastoreName;

    /**
     * 存储类型
     */
    @ApiModelProperty(value = "存储类型")
    private String driveType;

    /**
     * 总空间(gb)
     */
    @ApiModelProperty(value = "总空间(gb)")
    private Double spaceTotal;

    /**
     * 已用空间(gb)
     */
    @ApiModelProperty(value = "已用空间(gb)")
    private Double spaceUsed;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "关联唯一key")
    private String datastoreKey;

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
     * 是否已删除(0表示未删除，1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除，1表示已删除)")
    private Boolean isDeleted;
}

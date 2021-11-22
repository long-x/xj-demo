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
 * @title: TempDataCenterResourceOverview
 * @Author: shig
 * @description: 数据中心资源总览临时表
 * @Date: 2019/12/11 1:23 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "数据中心资源总览临时表 拓展类对象", description = "数据中心资源总览临时表 拓展类对象")
public class TempDataCenterResourceOverviewVO implements Serializable {

    private static final long serialVersionUID = -1913794890599066881L;
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
     * 裸金属服务器数量数量
     */
    @ApiModelProperty(value = "裸金属服务器数量数量")
    private Integer bareMetalServerNum;

    /**
     * 虚拟服务器数量
     */
    @ApiModelProperty(value = "虚拟服务器数量")
    private Integer virtualServerNum;

    /**
     * 存储服务器数量
     */
    @ApiModelProperty(value = "存储服务器数量")
    private Integer storageServerNum;

    /**
     * 路由器数量
     */
    @ApiModelProperty(value = "路由器数量")
    private Integer routerNum;

    /**
     * 交换机数量
     */
    @ApiModelProperty(value = "交换机数量")
    private Integer switchNum;

    /**
     * 防火墙数量
     */
    @ApiModelProperty(value = "防火墙数量")
    private Integer firewallNum;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "creat创建人e_user")
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
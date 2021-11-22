package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:集群对象
 *
 * @author xxj
 * @create 2019-11-13 10:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("iaas_cluster")
@ApiModel(value = "集群对象", description = "集群对象")
public class IaasCluster extends Model<IaasCluster> {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 集群名
     */
    @ApiModelProperty(value = "集群名")
    private String clusterName;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /**
     * 虚拟机总量
     */
    @ApiModelProperty(value = "虚拟机总量")
    private Integer vmNum;

    /**
     * 总cpu频率
     */
    @ApiModelProperty(value = "总cpu频率")
    private Double cpuTotal;

    /**
     * 已用cpu频率
     */
    @ApiModelProperty(value = "已用cpu频率")
    private Double cpuUsed;

    /**
     * 磁盘总量
     */
    @ApiModelProperty(value = "磁盘总量")
    private Double diskTotal;

    /**
     * 已用磁盘总量
     */
    @ApiModelProperty(value = "已用磁盘总量")
    private Double diskUsed;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryTotal;

    /**
     * 内存(MB)
     */
    @ApiModelProperty(value = "内存(MB)")
    private Double memoryUsed;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer score;

    /**
     * 关联唯一key
     */
    @ApiModelProperty(value = "关联唯一key")
    private String clusterKey;

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
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 18:20 2019/11/30
 **/
@Data
@Accessors(chain = true)
@TableName("iaas_network_segment_relationship")
@ApiModel(value = "iaas网络网段关系表对象", description = "iaas网络网段关系表对象")
public class IaasNetworkSegmentRelationship extends Model<IaasNetworkSegmentRelationship> {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群网络id")
    private Long clusterNetworkId;

    @ApiModelProperty("网段id")
    private Long networkSegmentId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

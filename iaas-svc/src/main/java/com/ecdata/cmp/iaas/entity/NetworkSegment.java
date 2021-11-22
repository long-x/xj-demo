package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Data
@Accessors(chain = true)
@TableName("iaas_network_segment")
@ApiModel(value = "网段对象", description = "网段对象")
public class NetworkSegment extends Model<NetworkSegment> {

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 网段名 */
    @ApiModelProperty(value = "网段名")
    private String segmentName;

    /** 网段 */
    @ApiModelProperty(value = "网段(如：xx.xx.xx.0/24)")
    private String segment;

    /** 网关 */
    @ApiModelProperty(value = "网关")
    private String gateway;

    /** 子网掩码 */
    @ApiModelProperty(value = "子网掩码")
    private String netmask;

    /** 域名服务器 */
    @ApiModelProperty(value = "域名服务器")
    private String dns;

    /** cidr */
    @ApiModelProperty(value = "cidr")
    private String cidr;
    /** 是否启用 */
    @ApiModelProperty(value = "是否启用(0:不启用;1:启用;)")
    private Integer enable;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

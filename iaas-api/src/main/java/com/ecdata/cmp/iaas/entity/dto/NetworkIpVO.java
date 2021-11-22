package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
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
@ApiModel(value = "网络IP对象", description = "网络IP对象")
public class NetworkIpVO implements Serializable {

    private static final long serialVersionUID = -1009859354182728073L;
    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 网段id */
    @ApiModelProperty(value = "网段id")
    private Long segmentId;

    /** ip地址 */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    /** ip值 */
    @ApiModelProperty(value = "ip值")
    private Long ipValue;

    /** 类型 */
    @ApiModelProperty(value = "类型(1:待定;2:虚拟机;3:网关;)")
    private Integer type;

    /** 是否被占用 */
    @ApiModelProperty(value = "是否被占用(0:可用;1:占用;)")
    private Integer used;

    /** 关联占用者 */
    @ApiModelProperty(value = "关联占用者")
    private Long relevance;

    @ApiModelProperty(value = "回收者")
    private Long recycleUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "回收时间")
    private Date recycleTime;

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

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;
}

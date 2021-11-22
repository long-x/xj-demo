package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:资源池存储表
 *
 * @author xxj
 * @create 2019-11-13 10:51
 */
@Data
@Accessors(chain = true)
@TableName("iaas_resource_pool_datastore")
@ApiModel(value = "资源池存储对象", description = "资源池存储对象")
public class IaasResourcePoolDatastore extends Model<IaasResourcePoolDatastore> {
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
     * 资源池id
     */
    @ApiModelProperty(value = "资源池id")
    private Long poolId;

    /**
     * 存储id
     */
    @ApiModelProperty(value = "存储id")
    private String datastoreId;

    /**
     * 分配存储总量(gb)
     */
    @ApiModelProperty(value = "分配存储总量(gb)")
    private Double spaceTotalAllocate;

    /**
     * 已用分配存储总量(gb)
     */
    @ApiModelProperty(value = "已用分配存储总量(gb)")
    private Double spaceUsedAllocate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
     * 是否已删除(0表示未删除,1表示已正常)
     *
     * @TableLogic:removeById逻辑删除调用，pkVal()也要有
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

package com.ecdata.cmp.iaas.entity.component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/13 11:21,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iaas_component_operation")
@ApiModel(value = "组件操作", description = "组件操作")
public class IaasComponentOperation extends Model<IaasComponentOperation> {

    private static final long serialVersionUID = 1856400096287290196L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;


    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

    /**
     * 脚本id
     */
    @ApiModelProperty("脚本id")
    private Long scriptId;

    /**
     * 操作名
     */
    @ApiModelProperty("操作名")
    private String operationName;

    /**
     * 操作
     */
    @ApiModelProperty("操作(create/configure/start/stop/restart/delete/other)")
    private String operation;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否已删除
     */
    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;


    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

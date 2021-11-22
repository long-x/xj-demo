package com.ecdata.cmp.iaas.entity.component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2019/11/13 13:45,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iaas_component_param_value")
@ApiModel(value = "组件参数值", description = "组件参数值")
public class IaasComponentParamValue extends Model<IaasComponentParamValue> {
    private static final long serialVersionUID = -3394400284821390764L;

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
     * 组件参数id
     */
    @ApiModelProperty("组件参数id")
    private Long paramId;

    /**
     * 组件参数值
     */
    @ApiModelProperty("组件参数值")
    private String paramValue;

    /**
     * 展示值
     */
    @ApiModelProperty("展示值")
    private String displayValue;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
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

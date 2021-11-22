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
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 * iaas模板虚机组件参数对象
 *
 * @author xxj
 * @create 2019-11-18 14:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_template_virtual_machine_component_param")
@ApiModel(value = "iaas模板虚机组件参数对象", description = "iaas模板虚机组件参数对象")
public class IaasTemplateVirtualMachineComponentParam extends Model<IaasTemplateVirtualMachineComponentParam> {
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("关联iaas组件参数id")
    private Long componentParamId;

    @ApiModelProperty("虚拟机组件id")
    private Long vmComponentId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("显示名")
    private String displayName;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("参数类型(int;double;string;boolean;password;text)")
    private String paramType;

    @ApiModelProperty("0：非必须；1：必须")
    private Boolean required;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("参数值列表(如：[{ paramvalue : 1 ， displayvalue : 1 ， unit : 个 ， sort : 1， remark : remark }])")
    private String valueList;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

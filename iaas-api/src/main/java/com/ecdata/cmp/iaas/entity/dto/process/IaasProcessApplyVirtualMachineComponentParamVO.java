package com.ecdata.cmp.iaas.entity.dto.process;

import com.alibaba.fastjson.JSONArray;
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
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请虚机组件参数表", description = "iaas流程申请虚机组件参数表")
public class IaasProcessApplyVirtualMachineComponentParamVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "配合前端树状")
    private Long componentId;

    @ApiModelProperty("配合前端树状")
    private Integer modifiable;

    @ApiModelProperty("配合前端树状")
    private Integer isShow;

    @ApiModelProperty("配合前端树状")
    private Integer sort;

    @ApiModelProperty("配合前端树状")
    private JSONArray valueSelect;

    @ApiModelProperty(value = "主键")
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

    @ApiModelProperty("参数类型(int;double;string;boolean;password)")
    private Double paramType;

    @ApiModelProperty("0：非必须；1：必须")
    private Integer required;

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

    @ApiModelProperty("前端对应id接就行，不作处理")
    private String key;
}

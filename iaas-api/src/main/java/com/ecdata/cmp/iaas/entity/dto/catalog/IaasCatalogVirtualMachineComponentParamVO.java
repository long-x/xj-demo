package com.ecdata.cmp.iaas.entity.dto.catalog;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@ApiModel(value = "iaas服务目录虚机组件参数表", description = "iaas服务目录虚机组件参数表")
public class IaasCatalogVirtualMachineComponentParamVO implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("关联iaas组件参数id")
    private Long componentParamId;

    @ApiModelProperty("关联iaas组件参数id")
    private Long componentId;

    @ApiModelProperty("虚拟机组件id")
    private Long vmComponentId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("显示名")
    private String displayName;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("默认值")
    private String remark;

    @ApiModelProperty("参数类型(int;double;string;Integer;password)")
    private String paramType;

    @ApiModelProperty("0：非必须；1：必须")
    private String required;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("参数值列表(如：[{ paramvalue : 1 ， displayvalue : 1 ， unit : 个 ， sort : 1， remark : remark }])")
    private String valueList;

    @ApiModelProperty("参数值列表")
    private JSONArray valueSelect;

    @ApiModelProperty("用户申请时是否可修改默认值(0表示不能修改，1表示可修改)")
    private Integer modifiable;

    @ApiModelProperty("用户申请时是否展示(0表示不展示，1表示展示)")
    private Integer isShow;

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

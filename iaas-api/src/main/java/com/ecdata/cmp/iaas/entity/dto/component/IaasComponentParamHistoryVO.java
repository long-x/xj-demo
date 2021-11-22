package com.ecdata.cmp.iaas.entity.dto.component;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 13:43,
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "组件参数历史", description = "组件参数历史")
public class IaasComponentParamHistoryVO implements Serializable {
    private static final long serialVersionUID = -3112740785532987615L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 组件参数id
     */
    @ApiModelProperty("组件参数id")
    private Long componentParamId;

    /**
     * 组件历史id
     */
    @ApiModelProperty("组件历史id")
    private Long componentHistoryId;

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
     * 参数名
     */
    @ApiModelProperty("参数名")
    private String paramName;

    /**
     * 显示名
     */
    @ApiModelProperty("显示名")
    private String displayName;

    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;

    /**
     * 参数类型(int;double;string;boolean;password;text)
     */
    @ApiModelProperty("参数类型(int;double;string;boolean;password;text)")
    private String paramType;

    /**
     * 0：非必须；1：必须
     */
    @ApiModelProperty("0：非必须；1：必须")
    private Boolean required;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     参数值列表
     */
    @ApiModelProperty("参数值列表(如：[{'paramValue': '1','displayValue': '1', 'unit': '个', 'sort': 1, 'remark': 'remark'}])'")
    private String valueList;


    @ApiModelProperty("下拉框选择")
    private JSONArray valueSelect;



    /**
     * 备注
     */
    @ApiModelProperty("排序")
    private String sort;

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
     * 插入历史表时间
     */
    @ApiModelProperty("插入历史表时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date historyTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;




}

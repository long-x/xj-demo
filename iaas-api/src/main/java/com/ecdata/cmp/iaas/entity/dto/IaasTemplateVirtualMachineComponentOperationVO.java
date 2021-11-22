package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 * 组件
 *
 * @author xxj
 * @create 2019-11-18 16:56
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas模板虚拟机组件操作表", description = "iaas模板虚拟机组件操作表")
public class IaasTemplateVirtualMachineComponentOperationVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    private Long scriptId;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("操作id")
    private Long operationId;

    @ApiModelProperty("模板虚拟机组件id")
    private Long vmComponentId;

    @ApiModelProperty("关联iaas组件id")
    private Long componentId;

    @ApiModelProperty("模板虚拟机脚本id")
    private Long vmComponentScriptId;

    @ApiModelProperty("操作名")
    private String operationName;

    @ApiModelProperty("操作(create/configure/start/stop/restart/delete/other)")
    private String operation;

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
    private Boolean isDeleted;

    @ApiModelProperty("前端key)")
    private String key;

    @ApiModelProperty("前端achieve)")
    private String achieve;

}

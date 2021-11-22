package com.ecdata.cmp.iaas.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/10 14:13
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组/vdc/项目/流程关联关系", description = "业务组/vdc/项目/流程关联关系")
public class BppvVO {


    /**
     * 业务组id
     */
    @ApiModelProperty(value = "业务组id")
    private long businessGroupId;

    /**
     * 业务组名称
     */
    @ApiModelProperty(value = "业务组名称")
    private String businessGroupName;

    /**
     * 流程KEY
     */
    @ApiModelProperty(value = "流程KEY")
    private String processDefinitionKey;

    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称")
    private String processDefinitionName;

    /**
     * 是否为项目
     */
    @ApiModelProperty(value = "是否为项目")
    private String isApp;

    /**
     * VDC ID
     */
    @ApiModelProperty(value = "VDC ID")
    private String vdcId;

    /**
     * VDC名称
     */
    @ApiModelProperty(value = "VDC名称")
    private String vdcName;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;


}

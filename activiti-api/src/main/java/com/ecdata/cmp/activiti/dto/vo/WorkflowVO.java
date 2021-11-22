package com.ecdata.cmp.activiti.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "工作流对象", description = "工作流对象")
public class WorkflowVO implements Serializable {

    private static final long serialVersionUID = -4815257887252438686L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 工作流名
     */
    @ApiModelProperty("工作流名")
    private String workflowName;

    /**
     * 流程定义键
     */
    @ApiModelProperty("流程定义键")
    private String processDefinitionKey;

    /**
     * 自定义流程操作
     */
    @ApiModelProperty("自定义流程操作")
    private String processOperation;

    /**
     * 自定义流程对象
     */
    @ApiModelProperty("自定义流程对象")
    private String processObject;

    /**
     * 是否已禁用(0:未禁用;1:已禁用)
     */
    @ApiModelProperty("是否已禁用(0:未禁用;1:已禁用)")
    private Integer isDisabled;

    /**
     * 类型(1:默认类型;2:集团审批类型;3:paas默认审批类型;)
     */
    @ApiModelProperty("类型(1:默认类型;2:集团审批类型;3:paas默认审批类型;)")
    private Integer type;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 步骤列表
     */
    @ApiModelProperty(value = "步骤列表")
    private List<WorkflowStepVO> workflowStepVOList;
}

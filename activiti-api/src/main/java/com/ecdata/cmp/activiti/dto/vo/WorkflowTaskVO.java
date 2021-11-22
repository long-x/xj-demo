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
 * @since 2020-04-15
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "工作流任务对象", description = "工作流任务对象")
public class WorkflowTaskVO implements Serializable {

    private static final long serialVersionUID = -6262217059770393527L;
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
     * 工作流id
     */
    @ApiModelProperty("工作流id")
    private Long workflowId;

    /**
     * 工作流步骤
     */
    @ApiModelProperty("工作流步骤")
    private Integer workflowStep;

    /**
     * 流程实例id
     */
    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 类型(1:单人可决定;2:一票否决;)
     */
    @ApiModelProperty("类型(1:单人可决定;2:一票否决;)")
    private Integer type;

    /**
     * 代理人id
     */
    @ApiModelProperty("代理人id")
    private Long assigneeId;

    /**
     * 代理人名
     */
    @ApiModelProperty("代理人名")
    private String assigneeName;

    /**
     * 结果
     */
    @ApiModelProperty("结果")
    private String outcome;

    /**
     * 意见
     */
    @ApiModelProperty("意见")
    private String comment;

    /**
     * 是否已审批(0:未审批;1:已审批)
     */
    @ApiModelProperty("是否已审批(0:未审批;1:已审批)")
    private Integer isApproved;

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
     * 候选列表
     */
    @ApiModelProperty(value = "候选列表")
    private List<WorkflowTaskCandidateVO> candidateList;

}

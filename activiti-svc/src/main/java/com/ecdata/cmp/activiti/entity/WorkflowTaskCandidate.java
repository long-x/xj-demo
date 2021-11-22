package com.ecdata.cmp.activiti.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2020-04-15
 */
@Data
@Accessors(chain = true)
@TableName("workflow_task_candidate")
@ApiModel(value = "工作流任务候选对象", description = "工作流任务候选对象")
public class WorkflowTaskCandidate extends Model<WorkflowTaskCandidate> {

    private static final long serialVersionUID = 1471042988623496405L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 工作流任务id
     */
    @ApiModelProperty("工作流任务id")
    private Long workflowTaskId;

    /**
     * 关联ID
     */
    @ApiModelProperty("关联ID")
    private Long relateId;

    /**
     * 关联名
     */
    @ApiModelProperty("关联名")
    private String relateName;

    /**
     * 关联方(如，类型:关联id)
     */
    @ApiModelProperty("关联方(如，类型:关联id)")
    private String relatedParty;

    /**
     * 类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)
     */
    @ApiModelProperty("类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)")
    private Integer type;

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
     * 通过注解方式和XML形式自定义的SQL不会自动加上逻辑条件
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

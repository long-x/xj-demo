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
 * @since 2020-01-06
 */
@Data
@Accessors(chain = true)
@TableName("workflow")
@ApiModel(value = "工作流对象", description = "工作流对象")
public class Workflow extends Model<Workflow> {

    private static final long serialVersionUID = -627243225647216911L;
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

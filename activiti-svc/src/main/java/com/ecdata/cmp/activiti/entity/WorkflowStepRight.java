package com.ecdata.cmp.activiti.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("workflow_step_right")
@ApiModel(value = "工作流步骤权限对象", description = "工作流步骤权限对象")
public class WorkflowStepRight extends Model<WorkflowStepRight> {

    private static final long serialVersionUID = 7046944703654384886L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 步骤ID
     */
    @ApiModelProperty("步骤ID")
    private Long stepId;

    /**
     * 关联ID
     */
    @ApiModelProperty("关联ID")
    private Long relateId;

    /**
     * 类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)
     */
    @ApiModelProperty("类型(1:用户;2:角色;3:部门;4:项目;5:业务组;)")
    private Integer type;

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
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

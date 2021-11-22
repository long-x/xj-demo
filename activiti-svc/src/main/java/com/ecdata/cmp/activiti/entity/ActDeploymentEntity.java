package com.ecdata.cmp.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-07-02
 */
@Data
@Accessors(chain = true)
@TableName("ACT_RE_DEPLOYMENT")
@ApiModel(value = "部署对象", description = "部署对象")
public class ActDeploymentEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField(value = "NAME_")
    private String name;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @TableField(value = "CATEGORY_")
    private String category;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField(value = "TENANT_ID_")
    private String tenantId;

    /**
     * 部署时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "部署时间")
    @TableField(value = "DEPLOY_TIME_")
    private Date deploymentTime;

}

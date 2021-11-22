package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 2019/11/26 15:34
 */
@Data
@Accessors(chain = true)
@TableName("sys_notification")
@ApiModel(value = "通知表对象", description = "通知表对象")
public class SysNotification extends Model<SysNotification> {

    private static final long serialVersionUID = 3518595960880621999L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群id")
    private Long clusterId;

    @ApiModelProperty("华为告警id")
    private Long csn;

    @ApiModelProperty("安全云告警id")
    private Long sangId;

    @ApiModelProperty("告警平台(1.华为云 2.安全云)")
    private Integer alertPlatform;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("来源类型(1其他；2告警；3邮件；4审批）")
    private Integer sourceType;

    @ApiModelProperty("通知类型(1:各自处理消息;2:统一处理消息;)")
    private Integer type;

    @ApiModelProperty("通知级别")
    private String level;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("消息详情")
    private String detail;

    @ApiModelProperty("状态(1:未处理;2:已处理;)")
    private Integer status;

    @ApiModelProperty("处理人")
    private Long operator;

    @ApiModelProperty("处理时间")
    private Date operationTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("通知时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notifyTime;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 通过注解方式和XML形式自定义的SQL不会自动加上逻辑条件
     */
    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

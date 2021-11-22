package com.ecdata.cmp.user.dto;

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
 * @Author: Gaspar
 * @Description:
 * @Date: 2020/3/2 14:29
 */
@Data
@Accessors(chain = true)
@TableName("sys_notify_manage")
@ApiModel(value = "公告表对象", description = "公告表对象")
public class SysNotifyManageVO implements Serializable  {

    private static final long serialVersionUID = 3808546410756754013L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群id")
    private Long clusterId;

    @ApiModelProperty("通知类型（1.告警通知  2.公告通知）")
    private Integer notifyType;

    @ApiModelProperty("告警平台(1.华为云 2.安全云)")
    private Integer alertPlatform;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String detail;

    @ApiModelProperty("通知方式：本平台")
    private Integer localPlatform;

    @ApiModelProperty("通知方式：邮件")
    private Integer mail;

    @ApiModelProperty("通知方式：短信")
    private Integer sms;

    @ApiModelProperty("告警级别：高")
    private Integer high;

    @ApiModelProperty("告警级别：中")
    private Integer medium;

    @ApiModelProperty("告警级别：低")
    private Integer low;

    @ApiModelProperty("告警级别：紧急")
    private Integer urgent;

    @ApiModelProperty("告警级别：重要")
    private Integer important;

    @ApiModelProperty("告警级别：次要")
    private Integer minor;

    @ApiModelProperty("告警级别：提示")
    private Integer prompt;

    @ApiModelProperty("通知方式")
    private String notifySendType;

    @ApiModelProperty("告警级别")
    private String alertLevel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("通知人")
    private Long notifyUser;

    @ApiModelProperty("通知人名字")
    private String notifyUserName;

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

    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Integer isDeleted;

}

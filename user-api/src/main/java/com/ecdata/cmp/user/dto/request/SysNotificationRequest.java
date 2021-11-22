package com.ecdata.cmp.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-06 10:40
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息请求对象", description = "消息请求对象")
public class SysNotificationRequest implements Serializable {


    private static final long serialVersionUID = -3224475524608680779L;
    @ApiModelProperty("集群id")
    private Long clusterId;

    @ApiModelProperty("华为告警id")
    private Long csn;


    @ApiModelProperty("告警平台(1.华为云 2.安全云)")
    private Integer alertPlatform;


    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("来源类型(1其他；2告警；3邮件；4审批）")
    private Integer sourceType;

    @ApiModelProperty("通知类型(1:各自处理消息;2:统一处理消息;)")
    private Integer type;

    @ApiModelProperty("通知级别")
    @Size(min = 0, max = 2, message = "字符串长度要求0到2之间。")
    private String level;

    @ApiModelProperty("消息内容")
    @NotNull
    private String message;

    @ApiModelProperty("消息详情")
    private String detail;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "对象名称")
    private List<String> names;

    @ApiModelProperty(value = "接收消息用户ids")
    private List<Long> userIds;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("通知时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notifyTime;

    @ApiModelProperty(value = "接收消息角色ids")
    private List<Long> roleIds;

    @ApiModelProperty(value = "接收消息项目ids")
    private List<Long> projectIds;

    @ApiModelProperty(value = "接收消息部门ids")
    private List<Long> departmentIds;

    @ApiModelProperty(value = "接收消息项目组ids")
    private List<Long> groupIds;


}

package com.ecdata.cmp.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ZhaoYX
 * @since 2020/4/28 14:23,
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "安全云消息请求对象", description = "安全云消息请求对象")
public class SysNoteSangforRequest extends SysNotificationRequest implements Serializable {

    private static final long serialVersionUID = 4643401990721271473L;

    @ApiModelProperty("安全云告警id")
    private Long sangId;
}

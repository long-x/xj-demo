package com.ecdata.cmp.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "系统日志", description = "系统日志")
public class FeignLog {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    /**
     * 用户姓名；冗余用
     */
    @ApiModelProperty(value = "用户姓名")
    private String username;

    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String method;

    /**
     * 请求URI
     */
    @ApiModelProperty(value = "请求URI")
    private String uri;

    /**
     * 类型(1:登入;2:正常请求;3:异常;4:警告;)
     */
    @ApiModelProperty(value = "类型(1:登入;2:正常请求;3:异常;4:警告;)")
    private Integer type;

    /**
     * 请求来源ip
     */
    @ApiModelProperty(value = "请求来源ip")
    private String ip;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 请求者
     */
    @ApiModelProperty(value = "请求者")
    private Long createUser;

    /**
     * 请求时间
     */
    @ApiModelProperty(value = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String params;

}
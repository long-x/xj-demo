package com.ecdata.cmp.apigateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value = "系统日志", description = "系统日志")
public class SysLog extends Model<SysLog> {
    private static final long serialVersionUID = 5733511057798266536L;
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
     * 登陆账号
     */
    @ApiModelProperty(value = "登陆账号")
    private String username;

    /**
     * 登陆名字
     */
    @ApiModelProperty(value = "登陆名字")
    private String displayName;

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

    /**
     * HTTP响应
     * ALTER TABLE `cmp_log`.`sys_log`
     * ADD COLUMN `http_result` LONGTEXT NULL DEFAULT NULL COMMENT 'HTTP响应' AFTER `display_name`;
     */
    @ApiModelProperty(value = "http响应")
    private String httpResult;

}
package com.ecdata.cmp.apigateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 14:09
 * @modified By：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("log_validity")
@ApiModel(value = "系统日志", description = "系统日志")
public class LogValidity extends Model<LogValidity> {


    private static final long serialVersionUID = -4512718427805890679L;


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
    private String userName;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 竹云accessToken
     */
    @ApiModelProperty(value = "竹云accessToken")
    private String accessToken;


    /**
     * 竹云refreshToken
     */
    @ApiModelProperty(value = "竹云refreshToken")
    private String refreshToken;


    /**
     * 有效期(s)
     */
    @ApiModelProperty(value = "有效期")
    private Long validityTime;


    /**
     * 请求时间
     */
    @ApiModelProperty(value = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;




}

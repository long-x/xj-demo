package com.ecdata.cmp.apigateway.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 15:04
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "", description = "系统日志拓展对象")
public class LogValidityVO implements Serializable {
    private static final long serialVersionUID = 7179116333302363228L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

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

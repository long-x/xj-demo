package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 10:54
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "竹云token对象", description = "竹云token对象")
public class BanTokenVO implements Serializable {
    private static final long serialVersionUID = -1884441968873284840L;


    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String accessToken;

    /**
     * token的id(刷新token用到)
     */
    @ApiModelProperty(value = "token的id")
    private String refreshToken;

    /**
     * 用户uid
     */
    @ApiModelProperty(value = "uid")
    private String uid;

    /**
     * 时效（秒）
     */
    @ApiModelProperty(value = "时效")
    private String expiresIn;


    /**
     * errcode（调用失败才有）
     */
    @ApiModelProperty(value = "code")
    private String errcode;

    /**
     * msg（调用失败才有）
     */
    @ApiModelProperty(value = "消息提示")
    private String msg;

}

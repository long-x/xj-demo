package com.ecdata.cmp.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @title: BimUserResp
 * @Author: shig
 * @description: 竹云用户响应 对象
 * @Date: 2020/3/5 7:43 下午
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "竹云用户响应 对象", description = "竹云用户响应 对象")
public class BimUserResp implements Serializable {
    private static final long serialVersionUID = -4284043466364720064L;
    @ApiModelProperty(value = "请求 ID")
    private String bimRequestId;

    @ApiModelProperty(value = "主键")
    private String uid;

    @ApiModelProperty(value = "主键ids")
    private List<String> userIdList;

    @ApiModelProperty(value = "结果码:0 为正常处理")
    private String resultCode;

    @ApiModelProperty(value = "处理的信息")
    private String message;

    @ApiModelProperty(value = "用户对象")
    private BimUserAccountResp account;
}
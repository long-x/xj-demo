package com.ecdata.cmp.iaas.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 11:34
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "传参封装对象", description = "传参封装对象")
public class RequestVO {

//    @ApiModelProperty(value = "tokenId")
//    private String tokenId;

    @ApiModelProperty(value = "运营面Token")
    private String ocToken;

    @ApiModelProperty(value = "运维面Token")
    private String omToken;

    @ApiModelProperty(value = "vdcId")
    private String vdcId;

    @ApiModelProperty(value = "projectId")
    private String projectId;

    //CPU：562958543421441  内存：562958543486979  磁盘：562958543618052
    @ApiModelProperty(value = "监控类型id/内存/cpu/磁盘")
    private String indicatorIds;

    @ApiModelProperty(value = "监控对象标识列表，对应资源管理实例ID")
    private String objIds;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "供应商Id")
    private String providerId;

    @ApiModelProperty(value = "间隔类型")
    private String interval;

    @ApiModelProperty(value = "虚拟机resId")
    private String resId;


}

package com.ecdata.cmp.user.dto.response;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @title: BimOrgResp
 * @Author: shig
 * @description: 竹云机构响应 对象
 * @Date: 2020/3/5 7:43 下午
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "竹云机构响应 对象", description = "竹云机构响应 对象")
public class BimOrgResp implements Serializable {
    private static final long serialVersionUID = -2620381503103712190L;
    @ApiModelProperty(value = "请求 ID")
    private String bimRequestId;

    @ApiModelProperty(value = "机构id")
    private String orgId;

    @ApiModelProperty(value = "机构ids")
    private List<String> orgIdList;

    @ApiModelProperty(value = "结果码:0 为正常处理")
    private String resultCode;

    @ApiModelProperty(value = "处理的信息")
    private String message;

    @ApiModelProperty(value = "组织机构")
    private BimOrgOrganizationResp organization;

}
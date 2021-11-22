package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.common.api.IntegerResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecdata.cmp.iaas.IaasConstant;
import com.ecdata.cmp.iaas.entity.dto.response.ProjectListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.ProjectResponse;

/**
 * @title: IaasProjectClient
 * @Author: shig
 * @description: iaas项目客户端
 * @Date: 2019/11/12 9:40 下午
 */
@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/project")
public interface IaasProjectClient {

}

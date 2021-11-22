package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 16:26
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "项目返回对象", description = "项目返回对象")
public class ProjectsVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "description")
    private String description;

    @ApiModelProperty(value = "domain_id")
    private String domainId;

    @ApiModelProperty(value = "enabled")
    private boolean enabled;

    @ApiModelProperty(value = "tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "is_shared")
    private boolean isShared;

    @ApiModelProperty(value = "tenant_name")
    private String tenantName;

    @ApiModelProperty(value = "create_user_id")
    private String createUserId;

    @ApiModelProperty(value = "create_user_name")
    private String createUserName;

    @ApiModelProperty(value = "regions")
    private List regions;

    @ApiModelProperty(value = "前端用key")
    private String key;
}

package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * API接口 GET /v2.0/security-groups
 * @author ：xuj
 * @date ：Created in 2019/12/4 16:57
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Project返回对象", description = "安全组返回对象")
public class SecurityGroupsVO {

    @ApiModelProperty(value = "安全组的id")
    private String id;

    @ApiModelProperty(value = "安全组名称，最大长度不超过255")
    private String name;

    @ApiModelProperty(value = "description")
    private String description;

    @ApiModelProperty(value = "租户id，只有管理员用户才允许指定非本租户的tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "资源详细信息")
    private List<SecurityGroupRulesVO> securityGroupRulesVOS;

    @ApiModelProperty(value = "资源创建时间")
    private String createdAt;

    @ApiModelProperty(value = "资源更新时间")
    private String updatedAt;

    @ApiModelProperty(value = "资源project_id")
    private String projectId;

}

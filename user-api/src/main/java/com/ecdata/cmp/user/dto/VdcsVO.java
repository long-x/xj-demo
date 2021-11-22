package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 15:43
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询VDC返回对象", description = "查询VDC返回对象")
public class VdcsVO {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "tag")
    private String tag;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "上级VDC ID")
    private String upperVdcId;

    @ApiModelProperty(value = "enabled")
    private boolean enabled;

    @ApiModelProperty(value = "域ID")
    private String domainId;

    @ApiModelProperty(value = "级别")
    private String level;

    @ApiModelProperty(value = "createUserId")
    private String createUserId;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private String createAt;

    @ApiModelProperty(value = "域名称")
    private String domainName;

    @ApiModelProperty(value = "thirdId")
    private String thirdId;

    @ApiModelProperty(value = "idpName")
    private String idpName;

    @ApiModelProperty(value = "区域ID")
    private String regionId;

    @ApiModelProperty(value = "enterpriseId")
    private String enterpriseId;

    @ApiModelProperty(value = "azId")
    private String azId;

    @ApiModelProperty(value = "enterpriseProjectId")
    private String enterpriseProjectId;

    @ApiModelProperty(value = "thirdType")
    private String thirdType;

    @ApiModelProperty(value = "前端key对应id")
    private String key;


}

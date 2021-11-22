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
 * @author ：xuj
 * @date ：Created in 2019/12/9 10:57
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "区域返回对象", description = "区域返回对象")
public class RegionsVO {

    @ApiModelProperty(value = "区域ID")
    private String id;

    @ApiModelProperty(value = "区域的父区域ID")
    private String parentRegionId;

    @ApiModelProperty(value = "区域描述")
    private String description;

    @ApiModelProperty(value = "区域的链接")
    private Map<String,Object> link;

    @ApiModelProperty(value = "区域类型")
    private List cloudInfras;

    @ApiModelProperty(value = "区域名")
    private Map<String,Object> locales;

    @ApiModelProperty(value = "区域的链接，包含next,previous,self")
    private List<Map<String,Object>> links;


}

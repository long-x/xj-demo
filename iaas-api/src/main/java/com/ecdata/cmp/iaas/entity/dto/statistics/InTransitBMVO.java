package com.ecdata.cmp.iaas.entity.dto.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/15 11:50
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "云平台资源统计 在途 裸金属", description = "云平台资源统计 在途 裸金属")
public class InTransitBMVO {

    /**
     * vcdID
     */
    @ApiModelProperty(value = "vcdID")
    private Long id;

    /**
     * vdc 名称
     */
    @ApiModelProperty(value = "名称")
    private String remark;

    /**
     * 裸金属类型
     */
    @ApiModelProperty(value = "裸金属类型")
    private String model;

    /**
     * 裸金属计数
     */
    @ApiModelProperty(value = "裸金属计数")
    private Long con;

}

package com.ecdata.cmp.iaas.entity.dto.employ;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/16 19:50
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "计算资源裸金属信息", description = "计算资源裸金属信息")
public class EmployBmVO {

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String remark;

    /**
     * 裸金属型号
     */
    @ApiModelProperty(value = "裸金属型号")
    private String model;


    /**
     * 裸金属数量
     */
    @ApiModelProperty(value = "裸金属数量")
    private Long bmcon;

}

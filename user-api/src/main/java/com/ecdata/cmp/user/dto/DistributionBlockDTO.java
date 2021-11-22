package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2019-12-05
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分布块数据传输对象", description = "分布块数据传输对象")
public class DistributionBlockDTO implements Serializable {

    private static final long serialVersionUID = 511959485840834681L;
    /** 块名 */
    @ApiModelProperty(value = "块名")
    private String blockName;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /** 比率 */
    @ApiModelProperty(value = "比率")
    private Double rate;

    /** 横向 */
    @ApiModelProperty(value = "横向")
    private String horizontal;

}

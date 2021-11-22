package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-12-05
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分布数据传输对象", description = "分布数据传输对象")
public class DistributionDTO implements Serializable {

    private static final long serialVersionUID = 7910356272986030472L;
    /**
     * 分布名
     */
    @ApiModelProperty(value = "分布名")
    private String distributionName;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Integer totalNum;

    /**
     * 分布块列表
     */
    @ApiModelProperty(value = "分布块列表")
    private List<DistributionBlockDTO> blockList;

}

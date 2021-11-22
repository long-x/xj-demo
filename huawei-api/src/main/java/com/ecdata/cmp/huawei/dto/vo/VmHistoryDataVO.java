package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 18:12
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机/宿主机历史性能对象", description = "虚拟机/宿主机历史性能对象")
public class VmHistoryDataVO {

    @ApiModelProperty(value = "最小值")
    private String min;

    @ApiModelProperty(value = "最大值")
    private String max;

    @ApiModelProperty(value = "平均值")
    private String avg;

    @ApiModelProperty(value = "历史值")
    private String history;


}

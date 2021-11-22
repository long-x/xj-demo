package com.ecdata.cmp.huawei.dto.availablezone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/12/3 19:35
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "主机状态", description = "主机状态")
public class Host implements Serializable {
    private static final long serialVersionUID = 9169842046532838551L;
    /**
     * 容量信息是否为空
     */
    @ApiModelProperty(value = "容量信息是否为空")
    private String isDataEmpty;

    /**
     * 正常状态物理主机数量
     */
    @ApiModelProperty(value = "正常状态物理主机数量")
    private Integer normal;

    /**
     * 关机状态物理主机数量
     */
    @ApiModelProperty(value = "关机状态物理主机数量")
    private Integer poweroff;

    /**
     * 故障状态物理主机数量
     */
    @ApiModelProperty(value = "故障状态物理主机数量")
    private Integer fault;

    /**
     * 其他状态物理主机数量
     */
    @ApiModelProperty(value = "其他状态物理主机数量")
    private Integer other;

    /**
     * 物理机采集时间
     */
    @ApiModelProperty(value = "物理机采集时间")
    private String collectTime;

    /**
     * 更新时间周期
     */
    @ApiModelProperty(value = "更新时间周期")
    private Long updateTime;

}

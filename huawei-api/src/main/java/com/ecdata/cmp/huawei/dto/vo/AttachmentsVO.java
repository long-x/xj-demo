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
 * @date ：Created in 2019/12/11 10:52
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机磁盘返回对象-挂卷信息", description = "虚拟机磁盘返回对象-挂卷信息")
public class AttachmentsVO {

    @ApiModelProperty(value = "卷id")
    private String id;

    @ApiModelProperty(value = "挂载云磁盘ID")
    private String volumeId;

    @ApiModelProperty(value = "挂载目录")
    private String device;

    @ApiModelProperty(value = "所属云服务器ID")
    private String serverId;
}

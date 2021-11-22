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
 * @date ：Created in 2019/12/11 10:56
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机磁盘返回对象-元数据", description = "虚拟机磁盘返回对象-元数据")
public class MetadataVO {

    @ApiModelProperty(value = "StorageType")
    private String StorageType;

    @ApiModelProperty(value = "readonly")
    private String readonly;

    @ApiModelProperty(value = "lun_wwn")
    private String lunWwn;

    @ApiModelProperty(value = "租赁")
    private String tenancy;

    @ApiModelProperty(value = "系统是否已满")
    private String sysIsServerVol;

    @ApiModelProperty(value = "附加模式")
    private String attachedMode;

}

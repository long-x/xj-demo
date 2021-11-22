package com.ecdata.cmp.iaas.entity.dto.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/26 11:33
 * @modified By：
 */
@Data
public class ResourceResultVO {


    @ApiModelProperty("cup")
    private int cpu;

    @ApiModelProperty("内存")
    private int memory;

    @ApiModelProperty("存储")
    private String systemDisk;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("裸金属数量")
    private int bmCon;

    @ApiModelProperty("裸金属型号")
    private String detailId;
}

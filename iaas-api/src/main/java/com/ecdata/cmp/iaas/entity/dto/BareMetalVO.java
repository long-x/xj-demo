package com.ecdata.cmp.iaas.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/24 10:45
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "裸金属对象", description = "裸金属对象")
public class BareMetalVO {

    @ApiModelProperty(value = "id")
    private Long id;

    //对应另一个接口的id
    @ApiModelProperty(value = "裸金属型号规格id")
    private String detailId;

    @ApiModelProperty(value = "裸金属id")
    private String nativieId;

    @ApiModelProperty(value = "裸金属名称")
    private String value;

    @ApiModelProperty(value = "裸金属状态")
    private String status;

    @ApiModelProperty(value = "所属项目id")
    private String tenantId;

    @ApiModelProperty(value = "私有ip")
    private String privateIps;

    @ApiModelProperty(value = "裸金属型号规格名称")
    private String detailName;

    @ApiModelProperty(value = "裸金属cpu")
    private String vcpus;

    @ApiModelProperty(value = "裸金属磁盘")
    private String disk;

    @ApiModelProperty(value = "裸金属型存储")
    private String ram;

    @ApiModelProperty(value = "裸金属浮动ip")
    private String floatingIp;

    @ApiModelProperty(value = "裸金属资源池名称")
    private String resourcePoolName;

    @ApiModelProperty(value = "裸金属区域名称")
    private String bizRegionName;

    @ApiModelProperty(value = "裸金属区域id")
    private String regionId;

    @ApiModelProperty(value = "裸金属可用分区")
    private String azoneInfo;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "裸金属前端名称")
    private String name;

    @ApiModelProperty("status :0-未绑定入库，1-已绑定入库")
    private Integer binded;

    @ApiModelProperty(value = "裸金属前端主键")
    private String key;

    @ApiModelProperty(value = "裸金属前端名称")
    private String title;

    @ApiModelProperty(value = "业务组名称")
    private String businessGroupName;

}

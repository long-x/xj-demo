package com.ecdata.cmp.iaas.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author zhaoyx
 * @Date 2019-12-06
 */

@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_bare_metal")
@ApiModel(value = "裸金属对象", description = "裸金属对象")
public class IaasBareMetal extends Model<IaasBareMetal> {

    private static final long serialVersionUID = 7692643801322777344L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("项目id")
    private Long projectId;

    @ApiModelProperty("区域id")
    private Long areaId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("裸金属型号规格id")
    private String detailId;

    @ApiModelProperty("裸金属id")
    private String nativieId;

    @ApiModelProperty("裸金属名称")
    private String name;

    @ApiModelProperty("裸金属状态")
    private String status;

    @ApiModelProperty("私有ip")
    private String privateIps;

    @ApiModelProperty("裸金属型号规格名称")
    private String detailName;

    @ApiModelProperty("裸金属cpu")
    private String vcpus;

    @ApiModelProperty("裸金属磁盘")
    private String disk;

    @ApiModelProperty("裸金属型存储")
    private String ram;

    @ApiModelProperty("裸金属浮动ip")
    private String floatingIp;

    @ApiModelProperty("裸金属资源池名称")
    private String resourcePoolName;

    @ApiModelProperty("裸金属区域名称")
    private String bizRegionName;

    @ApiModelProperty("裸金属区域id")
    private String regionId;

    @ApiModelProperty("裸金属可用分区")
    private String azoneInfo;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

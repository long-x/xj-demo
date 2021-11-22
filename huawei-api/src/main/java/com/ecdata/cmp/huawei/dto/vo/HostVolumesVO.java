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
 * @date ：Created in 2019/12/11 10:44
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机磁盘返回对象", description = "虚拟机磁盘返回对象")
public class HostVolumesVO {

    @ApiModelProperty(value = "卷id")
    private String id;

    @ApiModelProperty(value = "卷状态")
    private String status;

    @ApiModelProperty(value = "卷所属AZ")
    private String availabilityZone;

    @ApiModelProperty(value = "卷名称")
    private String displayName;

    @ApiModelProperty(value = "卷大小")
    private String size;

    @ApiModelProperty(value = "快照ID")
    private String snapshotId;

    @ApiModelProperty(value = "挂卷信息")
    private AttachmentsVO attachments;

    @ApiModelProperty(value = "元数据")
    private MetadataVO metadata;

    @ApiModelProperty(value = "创建卷的时间")
    private String createdAt;

    @ApiModelProperty(value = "卷类型")
    private String volumeType;

    @ApiModelProperty(value = "卷描述")
    private String displayDescription;

    /**
     * 关联的虚拟机信息
     *
     * @return
     */
    public String serverId() {
        if (this == null || this.attachments == null) {
            return "";
        }

        return this.attachments.getServerId();
    }
}

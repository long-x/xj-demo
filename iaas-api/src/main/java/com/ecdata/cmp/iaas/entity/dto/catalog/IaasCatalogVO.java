package com.ecdata.cmp.iaas.entity.dto.catalog;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-24 10:53
 */

@Data
@Accessors(chain = true)
@ApiModel(value = "iaas服务", description = "iaas服务")
public class IaasCatalogVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("供应商id")
    private Long providerId;

    @ApiModelProperty("供应商名称")
    private String providerName;

    @ApiModelProperty("服务目录名")
    private String catalogName;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("主要标签")
    private String mainTag;

    @ApiModelProperty("辅助标签(逗号分隔)")
    private String subTag;

    @ApiModelProperty("状态(1:保存;2:发布;)")
    private Integer state;

    @ApiModelProperty("初始类型(0:非初始模板; 1:顶级租户初始模板; 2:子租户租户初始模板;)")
    private Integer initType;

    @ApiModelProperty("权限类型(1:公开;2:依据服务目录权限表)")
    private Integer rightType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Boolean isDeleted;
}

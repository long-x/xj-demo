package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 18:20 2019/11/30
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas网络网段关系表对象", description = "iaas网络网段关系表对象")
public class IaasNetworkSegmentRelationshipVO implements Serializable {

    private static final long serialVersionUID = 5434258760344589311L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("集群网络id")
    @NotNull
    private Long clusterNetworkId;

    @ApiModelProperty("网段id")
    @NotNull
    private Long networkSegmentId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}

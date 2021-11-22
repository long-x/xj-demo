package com.ecdata.cmp.iaas.entity.dto.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xuxiaojian
 * @date 2020/3/3 14:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "网络要求表", description = "网络要求表")
public class IaasApplyNetworkAskVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("配置id")
    private Long configId;

    @ApiModelProperty("本机端口")
    private String nativePort;

    @ApiModelProperty("对方服务器")
    private String oppositeServer;

    @ApiModelProperty("对方端口")
    private String oppositePort;

    @ApiModelProperty("访问方式")
    private String accessMode;

    @ApiModelProperty("要求")
    private String requires;

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
    private boolean isDeleted;
}

package com.ecdata.cmp.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-10-16
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "同步项目请求", description = "同步项目请求对象")
public class SyncProjectRequest implements Serializable {
    private static final long serialVersionUID = 7356078414753593327L;

    /** 集群id */
    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    /** 租户id */
    @ApiModelProperty(value = "项目名列表")
    private List<String> nameList;
}

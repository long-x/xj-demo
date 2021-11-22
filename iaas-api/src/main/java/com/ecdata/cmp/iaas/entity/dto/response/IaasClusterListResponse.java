package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: iaasIaasCluster response
 * @Author: shig
 * @description: 集群 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasClusterListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasClusterVo> data;

    public IaasClusterListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasClusterListResponse(List<IaasClusterVo> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasClusterListResponse(ResultEnum resultEnum, List<IaasClusterVo> data) {
        super(resultEnum);
        this.data = data;
    }
}
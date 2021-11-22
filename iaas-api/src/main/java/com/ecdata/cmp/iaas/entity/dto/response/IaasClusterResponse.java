package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: IaasClusterV resp
 * @Author: shig
 * @description: 集群 响应对象
 * @Date: 2019/11/18 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasClusterResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasClusterVo data;

    public IaasClusterResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasClusterResponse(IaasClusterVo data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasClusterResponse(ResultEnum resultEnum, IaasClusterVo data) {
        super(resultEnum);
        this.data = data;
    }

}
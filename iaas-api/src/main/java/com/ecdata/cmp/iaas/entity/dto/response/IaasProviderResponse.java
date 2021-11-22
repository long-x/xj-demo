package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: iaasProvider response
 * @Author: shig
 * @description: 供应商 响应对象
 * @Date: 2019/11/12 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProviderResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private IaasProviderVO data;

    public IaasProviderResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProviderResponse(IaasProviderVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProviderResponse(ResultEnum resultEnum, IaasProviderVO data) {
        super(resultEnum);
        this.data = data;
    }

}
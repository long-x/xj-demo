package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: IaasVirtualDataCenterResponse
 * @Author: shig
 * @description: vdc
 * @Date: 2019/12/17 11:47 上午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasVirtualDataCenterResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasVirtualDataCenterVO data;

    public IaasVirtualDataCenterResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasVirtualDataCenterResponse(IaasVirtualDataCenterVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasVirtualDataCenterResponse(ResultEnum resultEnum, IaasVirtualDataCenterVO data) {
        super(resultEnum);
        this.data = data;
    }

}
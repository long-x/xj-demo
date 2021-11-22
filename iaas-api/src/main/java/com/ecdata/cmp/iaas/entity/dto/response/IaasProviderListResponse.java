package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: iaasProvider response
 * @Author: shig
 * @description: 供应商 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProviderListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasProviderVO> data;

    public IaasProviderListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProviderListResponse(List<IaasProviderVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProviderListResponse(ResultEnum resultEnum, List<IaasProviderVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
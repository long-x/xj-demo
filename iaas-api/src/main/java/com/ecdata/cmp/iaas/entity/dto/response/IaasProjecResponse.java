package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: IaasProjecResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/17 2:26 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProjecResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasProjectVO data;

    public IaasProjecResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProjecResponse(IaasProjectVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProjecResponse(ResultEnum resultEnum, IaasProjectVO data) {
        super(resultEnum);
        this.data = data;
    }

}
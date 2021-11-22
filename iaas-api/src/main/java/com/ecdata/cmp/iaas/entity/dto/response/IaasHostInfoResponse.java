package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 16:09
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasHostInfoResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasHostVO data;

    public IaasHostInfoResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasHostInfoResponse(IaasHostVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasHostInfoResponse(ResultEnum resultEnum, IaasHostVO data) {
        super(resultEnum);
        this.data = data;
    }

}

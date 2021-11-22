package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingRulesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 14:49
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasAccountingRulesServiceResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private IaasAccountingRulesVO data;

    public IaasAccountingRulesServiceResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasAccountingRulesServiceResponse(IaasAccountingRulesVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasAccountingRulesServiceResponse(ResultEnum resultEnum, IaasAccountingRulesVO data) {
        super(resultEnum);
        this.data = data;
    }
}

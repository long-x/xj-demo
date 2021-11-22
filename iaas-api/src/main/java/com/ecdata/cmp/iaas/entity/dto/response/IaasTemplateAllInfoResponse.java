package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述:获取模板虚拟机，组件及参数信息
 *
 * @author xxj
 * @create 2019-11-19 14:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IaasTemplateAllInfoResponse extends BaseResponse {

    @ApiModelProperty(value = "模板信息")
    private IaasTemplateVO data;

    public IaasTemplateAllInfoResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasTemplateAllInfoResponse(IaasTemplateVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasTemplateAllInfoResponse(ResultEnum resultEnum, IaasTemplateVO data) {
        super(resultEnum);
        this.data = data;
    }
}

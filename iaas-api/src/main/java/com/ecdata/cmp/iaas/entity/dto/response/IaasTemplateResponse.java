package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 15:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasTemplateResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Long templateId;

    public IaasTemplateResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasTemplateResponse(Long templateId) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.templateId = templateId;
    }

    public IaasTemplateResponse(ResultEnum resultEnum, Long templateId) {
        super(resultEnum);
        this.templateId = templateId;
    }
}

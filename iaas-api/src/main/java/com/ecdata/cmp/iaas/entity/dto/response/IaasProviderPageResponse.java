package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;

/**
 * @title: iaasProvider page response
 * @Author: shig
 * @description: 供应商 分页响应对象
 * @Date: 2019/11/12 11:25 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProviderPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasProviderVO> data;

    public IaasProviderPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProviderPageResponse(PageVO<IaasProviderVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProviderPageResponse(ResultEnum resultEnum, PageVO<IaasProviderVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

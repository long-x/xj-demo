package com.ecdata.cmp.iaas.entity.dto.response.component;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentHistoryVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/12/13 17:17,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentHisResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private IaasComponentHistoryVO data;

    public ComponentHisResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentHisResponse(IaasComponentHistoryVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentHisResponse(ResultEnum resultEnum, IaasComponentHistoryVO data) {
        super(resultEnum);
        this.data = data;
    }


}

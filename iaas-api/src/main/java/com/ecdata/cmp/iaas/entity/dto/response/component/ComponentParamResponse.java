package com.ecdata.cmp.iaas.entity.dto.response.component;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentParamVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/15 14:20,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentParamResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasComponentParamVO> data;

    public ComponentParamResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentParamResponse(List<IaasComponentParamVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentParamResponse(ResultEnum resultEnum, List<IaasComponentParamVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

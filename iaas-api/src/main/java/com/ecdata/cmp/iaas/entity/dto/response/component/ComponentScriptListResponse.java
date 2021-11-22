package com.ecdata.cmp.iaas.entity.dto.response.component;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/28 9:59,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentScriptListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasComponentScriptVO> data;

    public ComponentScriptListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentScriptListResponse(List<IaasComponentScriptVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentScriptListResponse(ResultEnum resultEnum, List<IaasComponentScriptVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

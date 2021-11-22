package com.ecdata.cmp.iaas.entity.dto.response.component;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/14 10:30,
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasComponentVO> data;

    public ComponentListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentListResponse(List<IaasComponentVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentListResponse(ResultEnum resultEnum, List<IaasComponentVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

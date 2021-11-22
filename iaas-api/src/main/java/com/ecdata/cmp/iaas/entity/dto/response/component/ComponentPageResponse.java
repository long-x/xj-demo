package com.ecdata.cmp.iaas.entity.dto.response.component;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Chao Wax on 2019/11/18
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentPageResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasComponentVO> data;

    public ComponentPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentPageResponse(PageVO<IaasComponentVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentPageResponse(ResultEnum resultEnum, PageVO<IaasComponentVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

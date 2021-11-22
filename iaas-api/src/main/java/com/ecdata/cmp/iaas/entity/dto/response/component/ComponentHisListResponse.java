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

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/12/3 13:11,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentHisListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasComponentHistoryVO> data;

    public ComponentHisListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentHisListResponse(List<IaasComponentHistoryVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentHisListResponse(ResultEnum resultEnum, List<IaasComponentHistoryVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

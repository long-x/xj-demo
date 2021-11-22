package com.ecdata.cmp.iaas.entity.dto.response.alert;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentHistoryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/12/17 14:12,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasAlertListResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasAlertVO> data;

    public IaasAlertListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasAlertListResponse(List<IaasAlertVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasAlertListResponse(ResultEnum resultEnum, List<IaasAlertVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

package com.ecdata.cmp.iaas.entity.dto.response.alert;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/12/6 16:33,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasAlertPageResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasAlertVO> data;

    public IaasAlertPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasAlertPageResponse(PageVO<IaasAlertVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasAlertPageResponse(ResultEnum resultEnum, PageVO<IaasAlertVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

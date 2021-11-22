package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 11:06
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasVirtualMachinePageResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasVirtualMachineVO> data;

    public IaasVirtualMachinePageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasVirtualMachinePageResponse(PageVO<IaasVirtualMachineVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasVirtualMachinePageResponse(ResultEnum resultEnum, PageVO<IaasVirtualMachineVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

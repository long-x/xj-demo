package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 13:46
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasVirtualMachineListResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasVirtualMachineVO> data;

    public IaasVirtualMachineListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasVirtualMachineListResponse(List<IaasVirtualMachineVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasVirtualMachineListResponse(ResultEnum resultEnum, List<IaasVirtualMachineVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

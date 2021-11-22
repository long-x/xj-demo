package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/19 16:06
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VDCVirtualMachineResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private VirtualMachineVO data;

    public VDCVirtualMachineResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VDCVirtualMachineResponse(VirtualMachineVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VDCVirtualMachineResponse(ResultEnum resultEnum, VirtualMachineVO data) {
        super(resultEnum);
        this.data = data;
    }

}

package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 11:43
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VDCVirtualMachineListResponse   extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<VirtualMachineVO> data;

    public VDCVirtualMachineListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VDCVirtualMachineListResponse(List<VirtualMachineVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VDCVirtualMachineListResponse(ResultEnum resultEnum, List<VirtualMachineVO> data) {
        super(resultEnum);
        this.data = data;
    }



}

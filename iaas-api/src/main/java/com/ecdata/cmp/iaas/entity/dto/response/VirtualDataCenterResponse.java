package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/27 15:35
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VirtualDataCenterResponse  extends BaseResponse {


    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasVirtualDataCenterVO data;

    public VirtualDataCenterResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VirtualDataCenterResponse(IaasVirtualDataCenterVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VirtualDataCenterResponse(ResultEnum resultEnum, IaasVirtualDataCenterVO data) {
        super(resultEnum);
        this.data = data;
    }

}

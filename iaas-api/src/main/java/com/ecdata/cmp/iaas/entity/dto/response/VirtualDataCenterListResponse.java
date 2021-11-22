package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: VirtualDataCenter
 * @Author: shig
 * @description:
 * @Date: 2019/12/13 5:20 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VirtualDataCenterListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasVirtualDataCenterVO> data;

    public VirtualDataCenterListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VirtualDataCenterListResponse(List<IaasVirtualDataCenterVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VirtualDataCenterListResponse(ResultEnum resultEnum, List<IaasVirtualDataCenterVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
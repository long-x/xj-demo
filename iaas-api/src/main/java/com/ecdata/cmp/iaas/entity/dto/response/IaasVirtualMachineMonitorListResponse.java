package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: iaasIaasVirtualMachineMonitor response
 * @Author: shig
 * @description: 虚拟机监控 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasVirtualMachineMonitorListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasVirtualMachineMonitorVO> data;

    public IaasVirtualMachineMonitorListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasVirtualMachineMonitorListResponse(List<IaasVirtualMachineMonitorVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasVirtualMachineMonitorListResponse(ResultEnum resultEnum, List<IaasVirtualMachineMonitorVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
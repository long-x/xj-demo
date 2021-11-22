package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: IaasVirtualMachineMonitor page response
 * @Author: shig
 * @description: 虚拟机监控 分页响应对象
 * @Date: 2019/11/25 11:25 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasVirtualMachineMonitorPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasVirtualMachineMonitorVO> data;

    public IaasVirtualMachineMonitorPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasVirtualMachineMonitorPageResponse(PageVO<IaasVirtualMachineMonitorVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasVirtualMachineMonitorPageResponse(ResultEnum resultEnum, PageVO<IaasVirtualMachineMonitorVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

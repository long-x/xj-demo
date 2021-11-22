package com.ecdata.cmp.iaas.entity.dto.response.process;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:申请服务详情
 *
 * @author xxj
 * @create 2019-11-26 9:59
 */
@Data
public class IaasProcessApplyDetailsResponse extends BaseResponse {

    @ApiModelProperty(value = "申请服务信息")
    private IaasProcessApplyVO processApplyVO;

    @ApiModelProperty(value = "创建返回的虚拟机信息")
    private IaasVirtualMachineVO iaasVirtualMachineVO;
}

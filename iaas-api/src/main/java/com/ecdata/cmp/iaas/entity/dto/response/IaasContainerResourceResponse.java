package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ContainerImageResourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ty
 * @description 查询供应商区域，集群，主机等信息的对象
 * @date 2019/11/18 14:58
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasContainerResourceResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private ContainerImageResourceVO data;

    public IaasContainerResourceResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasContainerResourceResponse(ContainerImageResourceVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasContainerResourceResponse(ResultEnum resultEnum, ContainerImageResourceVO data) {
        super(resultEnum);
        this.data = data;
    }
}

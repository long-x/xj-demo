package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: iaasIaasHost response
 * @Author: shig
 * @description: 主机 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasHostListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasHostVO> data;

    public IaasHostListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasHostListResponse(List<IaasHostVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasHostListResponse(ResultEnum resultEnum, List<IaasHostVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
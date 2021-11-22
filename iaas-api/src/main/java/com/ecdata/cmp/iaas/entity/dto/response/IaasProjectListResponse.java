package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author shig
 * @since 2019-12-13
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProjectListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<IaasProjectVO> data;

    public IaasProjectListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProjectListResponse(List<IaasProjectVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProjectListResponse(ResultEnum resultEnum, List<IaasProjectVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

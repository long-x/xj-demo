package com.ecdata.cmp.iaas.entity.dto.response.process;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 15:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasProcessPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasProcessApplyVO> data;

    public IaasProcessPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasProcessPageResponse(PageVO<IaasProcessApplyVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasProcessPageResponse(ResultEnum resultEnum, PageVO<IaasProcessApplyVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

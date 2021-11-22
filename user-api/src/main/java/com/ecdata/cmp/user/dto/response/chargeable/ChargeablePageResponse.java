package com.ecdata.cmp.user.dto.response.chargeable;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/11/27 13:29,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChargeablePageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysChargeableVO> data;

    public ChargeablePageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ChargeablePageResponse(PageVO<SysChargeableVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ChargeablePageResponse(ResultEnum resultEnum, PageVO<SysChargeableVO> data) {
        super(resultEnum);
        this.data = data;
    }


}

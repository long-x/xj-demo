package com.ecdata.cmp.user.dto.response.chargeable;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/27 13:27,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChargeableListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SysChargeableVO> data;

    public ChargeableListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ChargeableListResponse(List<SysChargeableVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ChargeableListResponse(ResultEnum resultEnum, List<SysChargeableVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

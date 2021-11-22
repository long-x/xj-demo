package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.BanUserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 16:20
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BanUserResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private BanUserVo data;

    public BanUserResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public BanUserResponse(BanUserVo data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public BanUserResponse(ResultEnum resultEnum, BanUserVo data) {
        super(resultEnum);
        this.data = data;
    }
}
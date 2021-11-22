package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.BanTokenVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 14:47
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BanTokenResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private BanTokenVO data;

    public BanTokenResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public BanTokenResponse(BanTokenVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public BanTokenResponse(ResultEnum resultEnum, BanTokenVO data) {
        super(resultEnum);
        this.data = data;
    }

}

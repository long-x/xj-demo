package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.NetworkIpVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetworkIpPageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<NetworkIpVO> data;

    public NetworkIpPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public NetworkIpPageResponse(PageVO<NetworkIpVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public NetworkIpPageResponse(ResultEnum resultEnum, PageVO<NetworkIpVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetworkSegmentPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<NetworkSegmentVO> data;

    public NetworkSegmentPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public NetworkSegmentPageResponse(PageVO<NetworkSegmentVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public NetworkSegmentPageResponse(ResultEnum resultEnum, PageVO<NetworkSegmentVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

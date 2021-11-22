package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;
import com.ecdata.cmp.iaas.entity.dto.ProjectVO;
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
public class NetworkSegmentResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private NetworkSegmentVO data;

    public NetworkSegmentResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public NetworkSegmentResponse(NetworkSegmentVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public NetworkSegmentResponse(ResultEnum resultEnum, NetworkSegmentVO data) {
        super(resultEnum);
        this.data = data;
    }
}

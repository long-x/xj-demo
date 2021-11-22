package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasNetworkSegmentRelationshipVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasNetworkSegmentRelationshipListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<IaasNetworkSegmentRelationshipVO> data;

    public IaasNetworkSegmentRelationshipListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasNetworkSegmentRelationshipListResponse(List<IaasNetworkSegmentRelationshipVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasNetworkSegmentRelationshipListResponse(ResultEnum resultEnum, List<IaasNetworkSegmentRelationshipVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

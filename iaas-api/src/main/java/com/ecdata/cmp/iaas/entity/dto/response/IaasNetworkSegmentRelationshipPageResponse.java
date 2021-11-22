package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasNetworkSegmentRelationshipVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasNetworkSegmentRelationshipPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasNetworkSegmentRelationshipVO> data;

    public IaasNetworkSegmentRelationshipPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasNetworkSegmentRelationshipPageResponse(PageVO<IaasNetworkSegmentRelationshipVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasNetworkSegmentRelationshipPageResponse(ResultEnum resultEnum, PageVO<IaasNetworkSegmentRelationshipVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

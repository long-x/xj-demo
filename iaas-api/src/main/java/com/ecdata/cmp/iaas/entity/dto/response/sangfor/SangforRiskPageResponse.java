package com.ecdata.cmp.iaas.entity.dto.response.sangfor;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2020/4/21 14:50,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SangforRiskPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SangforSecurityRiskVO> data;

    public SangforRiskPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SangforRiskPageResponse(PageVO<SangforSecurityRiskVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SangforRiskPageResponse(ResultEnum resultEnum, PageVO<SangforSecurityRiskVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

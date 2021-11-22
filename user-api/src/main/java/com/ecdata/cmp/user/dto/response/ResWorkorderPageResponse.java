package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import com.ecdata.cmp.user.dto.RoleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/11/20 11:03,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResWorkorderPageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ResWorkorderVO> data;

    public ResWorkorderPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResWorkorderPageResponse(PageVO<ResWorkorderVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResWorkorderPageResponse(ResultEnum resultEnum, PageVO<ResWorkorderVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

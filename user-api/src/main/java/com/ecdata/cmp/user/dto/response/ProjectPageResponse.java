package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.ProjectVO;
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
public class ProjectPageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ProjectVO> data;

    public ProjectPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjectPageResponse(PageVO<ProjectVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjectPageResponse(ResultEnum resultEnum, PageVO<ProjectVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

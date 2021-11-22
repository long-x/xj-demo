package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
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
public class ProjectResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private ProjectVO data;

    public ProjectResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjectResponse(ProjectVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjectResponse(ResultEnum resultEnum, ProjectVO data) {
        super(resultEnum);
        this.data = data;
    }
}

package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.ecdata.cmp.iaas.entity.dto.ProjectVO;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjectListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<ProjectVO> data;

    public ProjectListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjectListResponse(List<ProjectVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjectListResponse(ResultEnum resultEnum, List<ProjectVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

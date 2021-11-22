package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2020/4/17
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowTaskPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<WorkflowTaskVO> data;

    public WorkflowTaskPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public WorkflowTaskPageResponse(PageVO<WorkflowTaskVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public WorkflowTaskPageResponse(ResultEnum resultEnum, PageVO<WorkflowTaskVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

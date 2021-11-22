package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-02-24
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowTaskListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<WorkflowTaskVO> data;

    public WorkflowTaskListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public WorkflowTaskListResponse(List<WorkflowTaskVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public WorkflowTaskListResponse(ResultEnum resultEnum, List<WorkflowTaskVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

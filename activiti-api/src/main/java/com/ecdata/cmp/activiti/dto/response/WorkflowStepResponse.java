package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2020-04-22
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowStepResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private WorkflowStepVO data;

    public WorkflowStepResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public WorkflowStepResponse(WorkflowStepVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public WorkflowStepResponse(ResultEnum resultEnum, WorkflowStepVO data) {
        super(resultEnum);
        this.data = data;
    }

}

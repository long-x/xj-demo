package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
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
public class WorkflowTaskResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private WorkflowTaskVO data;

    public WorkflowTaskResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public WorkflowTaskResponse(WorkflowTaskVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public WorkflowTaskResponse(ResultEnum resultEnum, WorkflowTaskVO data) {
        super(resultEnum);
        this.data = data;
    }

}

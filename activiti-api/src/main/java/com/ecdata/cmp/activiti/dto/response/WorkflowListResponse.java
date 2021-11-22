package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.WorkflowVO;
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
 * @since 2020-01-08
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<WorkflowVO> data;

    public WorkflowListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public WorkflowListResponse(List<WorkflowVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public WorkflowListResponse(ResultEnum resultEnum, List<WorkflowVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

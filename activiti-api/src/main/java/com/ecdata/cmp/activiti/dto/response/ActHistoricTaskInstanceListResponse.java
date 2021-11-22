package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
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
public class ActHistoricTaskInstanceListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<ActHistoricTaskInstanceVO> data;

    public ActHistoricTaskInstanceListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ActHistoricTaskInstanceListResponse(List<ActHistoricTaskInstanceVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ActHistoricTaskInstanceListResponse(ResultEnum resultEnum, List<ActHistoricTaskInstanceVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

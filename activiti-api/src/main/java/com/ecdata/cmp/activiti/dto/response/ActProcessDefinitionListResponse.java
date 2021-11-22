package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.ActProcessDefinitionVO;
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
 * @since 2020-01-10
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActProcessDefinitionListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<ActProcessDefinitionVO> data;

    public ActProcessDefinitionListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ActProcessDefinitionListResponse(List<ActProcessDefinitionVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ActProcessDefinitionListResponse(ResultEnum resultEnum, List<ActProcessDefinitionVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

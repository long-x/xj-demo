package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.ActProcessDefinitionVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActProcessDefinitionResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private ActProcessDefinitionVO data;

    public ActProcessDefinitionResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ActProcessDefinitionResponse(ActProcessDefinitionVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ActProcessDefinitionResponse(ResultEnum resultEnum, ActProcessDefinitionVO data) {
        super(resultEnum);
        this.data = data;
    }

}

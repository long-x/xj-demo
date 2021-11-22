package com.ecdata.cmp.activiti.dto.response;

import com.ecdata.cmp.activiti.dto.vo.MyApplicationVO;
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
 * @since 2020-02-23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MyApplicationPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<MyApplicationVO> data;

    public MyApplicationPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public MyApplicationPageResponse(PageVO<MyApplicationVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public MyApplicationPageResponse(ResultEnum resultEnum, PageVO<MyApplicationVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

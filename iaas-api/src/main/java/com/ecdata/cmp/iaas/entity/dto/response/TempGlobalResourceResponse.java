package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempGlobalResourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: iaasProvider response
 * @Author: shig
 * @description: 整体资源临时表 响应对象
 * @Date: 2019/11/12 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempGlobalResourceResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempGlobalResourceVO data;

    public TempGlobalResourceResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempGlobalResourceResponse(TempGlobalResourceVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempGlobalResourceResponse(ResultEnum resultEnum, TempGlobalResourceVO data) {
        super(resultEnum);
        this.data = data;
    }

}
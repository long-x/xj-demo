package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysLogoStyleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: SysLogoStyle resp
 * @Author: shig
 * @description: 系统logo样式
 * @Date: 2019/11/22 9:36 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysLogoStyleResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private SysLogoStyleVO data;

    public SysLogoStyleResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysLogoStyleResponse(SysLogoStyleVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysLogoStyleResponse(ResultEnum resultEnum, SysLogoStyleVO data) {
        super(resultEnum);
        this.data = data;
    }
}
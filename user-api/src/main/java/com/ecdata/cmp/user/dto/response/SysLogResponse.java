package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysLogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: SysLogResponse resp
 * @Author: shig
 * @description: 系统日志响应对象
 * @Date: 2019/04/04 9:36 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysLogResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private SysLogVO data;

    public SysLogResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysLogResponse(SysLogVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysLogResponse(ResultEnum resultEnum, SysLogVO data) {
        super(resultEnum);
        this.data = data;
    }
}
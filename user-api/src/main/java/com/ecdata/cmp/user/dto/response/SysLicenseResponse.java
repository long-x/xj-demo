package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysLicenseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: SysLicenseResponse
 * @Author: shig
 * @description: 许可证响应对象
 * @Date: 2019/11/21 10:45 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysLicenseResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private SysLicenseVO data;

    public SysLicenseResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysLicenseResponse(SysLicenseVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysLicenseResponse(ResultEnum resultEnum, SysLicenseVO data) {
        super(resultEnum);
        this.data = data;
    }
}
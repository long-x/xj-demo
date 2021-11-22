package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/23 10:07
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessPoolResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private SysBusinessPoolVO data;

    public SysBusinessPoolResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }


    public SysBusinessPoolResponse(SysBusinessPoolVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessPoolResponse(ResultEnum resultEnum, SysBusinessPoolVO data) {
        super(resultEnum);
        this.data = data;
    }



}

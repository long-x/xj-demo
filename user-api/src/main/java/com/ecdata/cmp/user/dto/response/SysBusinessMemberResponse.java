package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/22 20:03
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessMemberResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private SysBusinessMemberVO data;

    public SysBusinessMemberResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }


    public SysBusinessMemberResponse(SysBusinessMemberVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessMemberResponse(ResultEnum resultEnum, SysBusinessMemberVO data) {
        super(resultEnum);
        this.data = data;
    }
}

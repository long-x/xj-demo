package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/22 20:29
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessMemberPageResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysBusinessMemberVO> data;

    public SysBusinessMemberPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessMemberPageResponse(PageVO<SysBusinessMemberVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessMemberPageResponse(ResultEnum resultEnum, PageVO<SysBusinessMemberVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

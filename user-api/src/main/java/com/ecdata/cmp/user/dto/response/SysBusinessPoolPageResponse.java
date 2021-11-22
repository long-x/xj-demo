package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/23 10:18
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessPoolPageResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysBusinessPoolVO> data;

    public SysBusinessPoolPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessPoolPageResponse(PageVO<SysBusinessPoolVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessPoolPageResponse(ResultEnum resultEnum, PageVO<SysBusinessPoolVO> data) {
        super(resultEnum);
        this.data = data;
    }


}

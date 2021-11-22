package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 16:47
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessGroupPageResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysBusinessGroupVO> data;

    public SysBusinessGroupPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessGroupPageResponse(PageVO<SysBusinessGroupVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessGroupPageResponse(ResultEnum resultEnum, PageVO<SysBusinessGroupVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

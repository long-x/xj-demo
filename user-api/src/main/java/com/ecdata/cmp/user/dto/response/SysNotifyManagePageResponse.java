package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 2020/3/2 15:39
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysNotifyManagePageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysNotifyManageVO> data;

    public SysNotifyManagePageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysNotifyManagePageResponse(PageVO<SysNotifyManageVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysNotifyManagePageResponse(ResultEnum resultEnum, PageVO<SysNotifyManageVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

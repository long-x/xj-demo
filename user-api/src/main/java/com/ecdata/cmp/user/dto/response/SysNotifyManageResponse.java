package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 2020/3/3 10:40
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysNotifyManageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private SysNotifyManageVO data;

    public SysNotifyManageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysNotifyManageResponse(SysNotifyManageVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysNotifyManageResponse(ResultEnum resultEnum, SysNotifyManageVO data) {
        super(resultEnum);
        this.data = data;
    }
}

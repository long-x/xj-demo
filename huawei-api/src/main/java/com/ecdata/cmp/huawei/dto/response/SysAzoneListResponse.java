package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import com.ecdata.cmp.huawei.dto.availablezone.SysAzone;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/3 11:04
 * @modified By：
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysAzoneListResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SysAzone> data;

    public SysAzoneListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysAzoneListResponse(List<SysAzone> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysAzoneListResponse(ResultEnum resultEnum, List<SysAzone> data) {
        super(resultEnum);
        this.data = data;
    }
}
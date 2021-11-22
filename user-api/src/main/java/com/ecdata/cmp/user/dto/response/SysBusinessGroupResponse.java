package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 16:35
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessGroupResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private SysBusinessGroupVO data;

    public SysBusinessGroupResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }


    public SysBusinessGroupResponse(SysBusinessGroupVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessGroupResponse(ResultEnum resultEnum, SysBusinessGroupVO data) {
        super(resultEnum);
        this.data = data;
    }


}

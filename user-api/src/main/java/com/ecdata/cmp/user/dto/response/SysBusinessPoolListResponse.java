package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/23 10:11
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessPoolListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SysBusinessPoolVO> data;

    public SysBusinessPoolListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessPoolListResponse(List<SysBusinessPoolVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessPoolListResponse(ResultEnum resultEnum, List<SysBusinessPoolVO> data) {
        super(resultEnum);
        this.data = data;
    }


}

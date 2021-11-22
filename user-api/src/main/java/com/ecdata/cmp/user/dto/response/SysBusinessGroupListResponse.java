package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 17:40
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessGroupListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SysBusinessGroupVO> data;

    public SysBusinessGroupListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessGroupListResponse(List<SysBusinessGroupVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessGroupListResponse(ResultEnum resultEnum, List<SysBusinessGroupVO> data) {
        super(resultEnum);
        this.data = data;
    }



}

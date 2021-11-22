package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/22 20:21
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessMemberListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SysBusinessMemberVO> data;

    public SysBusinessMemberListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysBusinessMemberListResponse(List<SysBusinessMemberVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessMemberListResponse(ResultEnum resultEnum, List<SysBusinessMemberVO> data) {
        super(resultEnum);
        this.data = data;
    }


}

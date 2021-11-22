package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<UserVO> data;

    public UserListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public UserListResponse(List<UserVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public UserListResponse(ResultEnum resultEnum, List<UserVO> data) {
        super(resultEnum);
        this.data = data;
    }
}

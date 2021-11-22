package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.dto.SysLogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: SysLogPageResponse
 * @Author: shig
 * @description: 日志分页
 * @Date: 2019/12/3 4:43 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysLogPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysLogVO> data;

    public SysLogPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SysLogPageResponse(PageVO<SysLogVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysLogPageResponse(ResultEnum resultEnum, PageVO<SysLogVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
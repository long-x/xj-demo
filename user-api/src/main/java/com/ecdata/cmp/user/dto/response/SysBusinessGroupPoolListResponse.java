package com.ecdata.cmp.user.dto.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/29 20:39
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysBusinessGroupPoolListResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<SysBusinessGroupResourcePoolVO> data;

    public SysBusinessGroupPoolListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }


    public SysBusinessGroupPoolListResponse(PageVO<SysBusinessGroupResourcePoolVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SysBusinessGroupPoolListResponse(ResultEnum resultEnum, PageVO<SysBusinessGroupResourcePoolVO> data) {
        super(resultEnum);
        this.data = data;
    }

}

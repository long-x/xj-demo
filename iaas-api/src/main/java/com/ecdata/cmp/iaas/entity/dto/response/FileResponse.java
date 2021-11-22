package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Date: 2019/11/18 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FileResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private FileVo data;

    public FileResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public FileResponse(FileVo data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public FileResponse(ResultEnum resultEnum, FileVo data) {
        super(resultEnum);
        this.data = data;
    }

}
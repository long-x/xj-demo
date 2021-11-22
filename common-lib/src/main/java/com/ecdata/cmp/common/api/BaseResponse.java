package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.enums.Code;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author honglei
 * @since 2019-08-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码")
    @Builder.Default
    private Integer code = 0;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String message;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private Boolean success = true;

    public boolean isSuccess() {
        this.success = code == 0;
        return success;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.isSuccess();
    }

    public BaseResponse(Code resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.isSuccess();
    }

    public void setResultEnum(Code resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.isSuccess();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
        this.isSuccess();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

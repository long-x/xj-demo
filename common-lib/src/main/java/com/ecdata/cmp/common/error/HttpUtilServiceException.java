package com.ecdata.cmp.common.error;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.Code;
import com.ecdata.cmp.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @author honglei
 * @since 2019-08-14
 */
public class HttpUtilServiceException extends ServiceException {

    public HttpUtilServiceException(Code code) {
        super(code);
    }

    public HttpUtilServiceException(String msg) {
        super(msg);
    }

    public HttpUtilServiceException(Code code, String msg) {
        super(code,msg);
    }
}

package com.ecdata.cmp.common.error;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.Code;
import com.ecdata.cmp.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @author honglei
 * @since 2019-08-14
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;
    /**
     * get resultEnum
     */
    @Getter
    private final BaseResponse statusCode;

    public ServiceException(String msg) {
        this(msg, ResultEnum.INTERNAL_SERVER_ERROR,null);
    }

    public ServiceException(String msg, Throwable cause) {
        this(msg, ResultEnum.INTERNAL_SERVER_ERROR,cause);
    }

    public ServiceException(Code resultEnum) {
        this(resultEnum.getMessage(),resultEnum,null);
    }

    public ServiceException(Code resultEnum, String msg) {
        this(msg,resultEnum,null);
    }

    public ServiceException(Code resultEnum, Throwable cause) {
        this(resultEnum.getMessage(),resultEnum,cause);
    }

    public ServiceException(String msg, Code resultEnum, Throwable cause){
        super(msg, cause);
        this.statusCode = new BaseResponse();
        statusCode.setCode(resultEnum.getCode());
        statusCode.setMessage(resultEnum.getMessage());
    }

    /**
     * for better performance
     *
     * @return Throwable
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    /**
     * @return fillInStackTrace
     */
    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}

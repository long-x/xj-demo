package com.ecdata.cmp.common.auth;

import com.ecdata.cmp.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @author honglei
 * @since 2019-08-14
 */
public class PermissionDeniedException extends RuntimeException {
    /**
     * resultCode
     */
    @Getter
    private final ResultEnum resultEnum;

    public PermissionDeniedException(String message) {
        super(message);
        this.resultEnum = ResultEnum.UNAUTHORIZED;
    }

    public PermissionDeniedException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }

    public PermissionDeniedException(ResultEnum resultEnum, Throwable cause) {
        super(cause);
        this.resultEnum = resultEnum;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

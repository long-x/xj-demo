package com.ecdata.cmp.common.error;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.PermissionDeniedException;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author honglei
 * @since 2019-08-14
 */

@RestControllerAdvice
public class GlobalExceptionTranslator {

    /**
     * LOGGER
     */
    private static final ILogger LOGGER = SLoggerFactory.getLogger(GlobalExceptionTranslator.class);

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse> handleError(MissingServletRequestParameterException e) {
        LOGGER.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse
                .builder()
                .code(ResultEnum.PARAM_MISS.getCode())
                .message(message)
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> handleError(MethodArgumentTypeMismatchException e) {
        LOGGER.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse
                .builder()
                .code(ResultEnum.PARAM_TYPE_ERROR.getCode())
                .message(message)
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleError(MethodArgumentNotValidException e) {
        LOGGER.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse
                .builder()
                .code(ResultEnum.PARAM_VALID_ERROR.getCode())
                .message(message)
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse> handleError(BindException e) {
        LOGGER.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse
                .builder()
                .code(ResultEnum.PARAM_BIND_ERROR.getCode())
                .message(message)
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleError(ConstraintViolationException e) {
        LOGGER.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse
                .builder()
                .code(ResultEnum.PARAM_VALID_ERROR.getCode())
                .message(message)
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse> handleError(NoHandlerFoundException e) {
        LOGGER.error("404 Not Found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse
                .builder()
                .code(ResultEnum.NOT_FOUND.getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> handleError(HttpMessageNotReadableException e) {
        LOGGER.error("Message Not Readable", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse
                .builder()
                .code(ResultEnum.MSG_NOT_READABLE.getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> handleError(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("Request Method Not Supported", e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(BaseResponse
                .builder()
                .code(ResultEnum.METHOD_NOT_SUPPORTED.getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<BaseResponse> handleError(HttpMediaTypeNotSupportedException e) {
        LOGGER.error("Media Type Not Supported", e);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(BaseResponse
                .builder()
                .code(ResultEnum.UNSUPPORTED_MEDIA_TYPE.getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<BaseResponse> handleError(ServiceException e) {
        LOGGER.error("Service Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse
                .builder()
                .code(e.getStatusCode().getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return BaseResponse
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<BaseResponse> handleError(PermissionDeniedException e) {
        LOGGER.error("Permission Denied", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse
                .builder()
                .code(e.getResultEnum().getCode())
                .message(e.getMessage())
                .build());
    }

    /**
     * @param e e
     * @return resultCode
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponse> handleError(Throwable e) {
        LOGGER.error("Internal Server Error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse
                .builder()
                .code(ResultEnum.INTERNAL_SERVER_ERROR.getCode())
                .message(e.getMessage())
                .build());
    }
}

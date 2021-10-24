package com.seanco.bfcm.exception;

import com.seanco.bfcm.vo.ResponseInfo;
import com.seanco.bfcm.vo.ResponseInfoEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseInfo exceptionHandler(Exception exception) {
        if (exception instanceof GlobalException) {
            GlobalException e = (GlobalException) exception;
            return ResponseInfo.error(e.getResponseInfoEnum());
        } else if (exception instanceof BindException) {
            BindException e = (BindException) exception;
            ResponseInfo responseInfo = ResponseInfo.error(ResponseInfoEnum.BIND_ERROR);
            responseInfo.setMsg("parameter validation failed: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return responseInfo;
        }
        return ResponseInfo.error(ResponseInfoEnum.ERROR);
    }
}

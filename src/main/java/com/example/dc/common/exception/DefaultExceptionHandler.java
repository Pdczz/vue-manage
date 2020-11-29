package com.example.dc.common.exception;

import com.example.dc.common.enums.ExceptionEnum;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(LyException.class)
    @ResponseBody
    public ExceptionEnum handleAuthorizationException(UnauthorizedException e) {
        //String message = "权限认证失败";
        return ExceptionEnum.AUTHRO_FAILE;
    }
}


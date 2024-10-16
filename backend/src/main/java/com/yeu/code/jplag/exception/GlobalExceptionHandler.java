
package com.yeu.code.jplag.exception;

import com.yeu.code.jplag.vo.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({RestException.class})
    public Response handlerRestException(RestException e, HttpServletRequest request) {
        e.setInfo(request.getServletPath());
        e.printStackTrace();
        return new Response(e.getCode(), e.getMsg(), e.getData());
    }

    @ExceptionHandler({Exception.class})
    public Response handlerException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        RestException.RequestExceptionInfo info = new RestException.RequestExceptionInfo(request.getServletPath());
        return new Response(500, "服务器内部错误", info);
    }
}

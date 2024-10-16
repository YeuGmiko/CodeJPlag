
package com.yeu.code.jplag.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestException extends RuntimeException {
    private Integer code;
    private String msg;
    private RequestExceptionInfo data;

    public RestException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public void setInfo(String path) {
        this.data = new RequestExceptionInfo(path);
    }

    @Data
    public static class RequestExceptionInfo {
        private LocalDateTime timestamp = LocalDateTime.now();
        private String path;

        public RequestExceptionInfo(String path) {
            this.path = path;
        }
    }
}

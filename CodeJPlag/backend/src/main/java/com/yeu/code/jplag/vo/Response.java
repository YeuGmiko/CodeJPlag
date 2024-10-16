
package com.yeu.code.jplag.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class Response<T> {
    private Integer code;
    private String msg;
    private T data;
    private static final Integer DEFAULT_SUCCESS_CODE = 200;
    private static final String DEFAULT_SUCCESS_MSG = "Success";

    public Response() {
        this.code = DEFAULT_SUCCESS_CODE;
        this.msg = "Success";
    }

    public Response(Integer code) {
        this.msg = "Success";
        this.code = code;
    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}

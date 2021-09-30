package com.xuhaoming.hami.web.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 * @author xuhaoming
 * @since 2020/6/2
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Integer code = 0;
    private String msg = "服务器错误，请稍后再试";

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super();
        this.msg = message;
    }

    public BusinessException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.msg = message;
    }

    public BusinessException(int code, String message) {
        super();
        this.code = code;
        this.msg = message;
    }

    public BusinessException(HttpStatus status, int code, String message) {
        super();
        this.status = status;
        this.code = code;
        this.msg = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

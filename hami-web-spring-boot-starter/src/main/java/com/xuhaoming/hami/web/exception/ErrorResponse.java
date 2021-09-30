package com.xuhaoming.hami.web.exception;

/**
 * 错误返回对象
 *
 * @author xuhaoming
 * @date 2021/9/15 14:22
 */
public class ErrorResponse {

    private Integer code;

    private String msg;

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

    public ErrorResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

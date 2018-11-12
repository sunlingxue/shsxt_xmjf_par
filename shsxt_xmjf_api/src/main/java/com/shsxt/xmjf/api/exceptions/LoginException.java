package com.shsxt.xmjf.api.exceptions;


public class LoginException extends RuntimeException{
    private Integer code=305;
    private String msg="user not login";

    public LoginException() {
        super("user not login");
    }

    public LoginException(Integer code) {
        super("user not login");
        this.code = code;
    }

    public LoginException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public LoginException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
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

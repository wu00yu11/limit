package com.learn.limit.constant;

/**
 * @author jingjing.zhang
 */

public enum RespType {
    SUCCESS(200,"成功"),
    FAIL(400,"失败"),
    SERVER_EXCEPTION(500,"系统繁忙"),
            ;
    private final int code;
    private final String msg;

    RespType(int code, String msg) {
        this.code = code;
        this.msg  = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

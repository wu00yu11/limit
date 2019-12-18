package com.learn.limit.vo;

import com.learn.limit.constant.RespType;

import java.io.Serializable;
import java.util.List;

/**
 * @program: limit
 * @description:
 * @author: zjj
 * @create: 2019-12-18 11:01
 **/
public class Result implements Serializable {

    private static final long serialVersionUID = 7609289798211072591L;

    private Integer status;
    private String msg;
    private List<Object> data;

    private Result(Integer status, String msg, List<Object> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(RespType respType, List<Object> data) {
        this(respType.getCode(), respType.getMsg(), data);
    }

    public Result(RespType respType) {
        this(respType.getCode(), respType.getMsg(), null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}

package com.yy.bscRobot.entity;

import java.io.Serializable;

public class Result implements Serializable {

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private int code;
    private String message;
    private Object data;

    public static Result success(Object data)
    {
        Result result = new Result();
        result.code=200;
        result.message="OK";
        result.data=data;
        return result;
    }

    public static Result fail(int code,String message)
    {
        Result result = new Result();
        result.code=code;
        result.message=message;
        result.data=null;
        return result;
    }

}

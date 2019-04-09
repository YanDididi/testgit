package com.moyu.media.core.result;

public class Result {
    protected int status; //0失败;1:成功;
    protected int code;
    protected String msg;
    protected Object data;

    public enum Status {
        SUCCESS(1),
        FAIL(0);
        public int status;

        Status(int status) {
            this.status = status;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}

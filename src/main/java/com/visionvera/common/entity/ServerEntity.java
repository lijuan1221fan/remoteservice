package com.visionvera.common.entity;

/**
 * Created by jlm on 2019-09-20 13:14
 */
public class ServerEntity {

    private boolean result;
    private String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ServerEntity(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public ServerEntity() {
    }

    @Override
    public String toString() {
        return "ServerEntity{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}

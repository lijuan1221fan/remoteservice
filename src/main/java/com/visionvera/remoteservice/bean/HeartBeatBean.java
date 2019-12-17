package com.visionvera.remoteservice.bean;

import java.sql.Date;

public class HeartBeatBean extends TypeBean {

    private String dateTime;

    private String version;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

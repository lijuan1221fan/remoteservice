package com.visionvera.remoteservice.bean;

import java.io.Serializable;

public class ServiceCenterDetailBean implements Serializable {

    /**
     * 中心名称
     */
    private String name;
    /**
     * 正在受理的业务名称
     */
    private String businessName;
    /**
     * 当前等待人数
     */
    private Integer waitNumber;
    /**
     * 今日办理业务数
     */
    private Integer todayBusinessNumber;
    /**
     * 累计办理业务数
     */
    private Integer totalBusinessNumber;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getWaitNumber() {
        return waitNumber;
    }

    public void setWaitNumber(Integer waitNumber) {
        this.waitNumber = waitNumber;
    }

    public Integer getTodayBusinessNumber() {
        return todayBusinessNumber;
    }

    public void setTodayBusinessNumber(Integer todayBusinessNumber) {
        this.todayBusinessNumber = todayBusinessNumber;
    }

    public Integer getTotalBusinessNumber() {
        return totalBusinessNumber;
    }

    public void setTotalBusinessNumber(Integer totalBusinessNumber) {
        this.totalBusinessNumber = totalBusinessNumber;
    }
}
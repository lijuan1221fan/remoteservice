package com.visionvera.common.enums;

public enum ServiceCenterStatusEnum {

    /**
     *
     */
    Doing(1, "办理中"),
    /**
     *
     */
    Waiting(2, "等待中"),
    /**
     *
     */
    Free(3, "空闲中");

    private Integer status;
    private String desc;

    ServiceCenterStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

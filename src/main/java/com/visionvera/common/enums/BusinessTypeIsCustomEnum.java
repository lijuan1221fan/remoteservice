package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-05-14 09:34
 */
public enum BusinessTypeIsCustomEnum {

    IsCUSTOM(1,"后台配置"),
    IsNotCUSTOM(2,"自由拖拽");

    private Integer value;
    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    BusinessTypeIsCustomEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

}


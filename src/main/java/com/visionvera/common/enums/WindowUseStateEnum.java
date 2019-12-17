package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-08-20 15:42
 */
public enum WindowUseStateEnum {

    InUse(1, "使用中"),
    Free(2, "空闲"),
    Wait(3, "等待");
    private Integer value;
    private String name;

    WindowUseStateEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static WindowUseStateEnum valueToEnum(int value) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getValue() == value) {
                return values()[k];
            }
        }
        return null;
    }

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

}

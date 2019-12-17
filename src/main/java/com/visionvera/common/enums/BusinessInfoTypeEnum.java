package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-03-27 10:14
 */
public enum BusinessInfoTypeEnum {

    Nomoral(1, "正常取号"),
    Appointment(2, "预约取号");

    private Integer value;
    private String name;

    BusinessInfoTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static BusinessInfoTypeEnum valueToEnum(int value) {
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

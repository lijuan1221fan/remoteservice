package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-06-19 17:56
 */
public enum SystemVersionEnum {

    SixTeen("1", "16位"),
    SixtyFour("2", "64位");
    private String value;
    private String name;

    SystemVersionEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static SystemVersionEnum valueToEnum(String value) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getValue() == value) {
                return values()[k];
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

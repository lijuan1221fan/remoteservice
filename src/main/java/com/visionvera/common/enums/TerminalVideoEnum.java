package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-09-25 09:29
 */
public enum TerminalVideoEnum {

    NORMAL(0, "视频正常"),
    ABNORMAL(1, "视频异常");

    private Integer value;
    private String name;

    TerminalVideoEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TerminalVideoEnum valueToEnum(int value) {
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

package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-09-25 09:27
 */
public enum TerminalStatusEnum {

    NORMAL(0, "入网空闲"),
    ABNORMAL(1, "业务繁忙"),
    NODEVICE(2, "无法连接");

    private Integer value;
    private String name;

    TerminalStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TerminalStatusEnum valueToEnum(int value) {
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

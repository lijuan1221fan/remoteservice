package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-09-23 14:33
 */
public enum AndroidDetectionPrintModeEnum {

    NORMAL(0, "打印机模块正常"),
    ABNORMAL(1, "设备异常"),
    NODEVICE(2, "设备未连接");

    private Integer value;
    private String name;

    AndroidDetectionPrintModeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static AndroidDetectionPrintModeEnum valueToEnum(int value) {
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

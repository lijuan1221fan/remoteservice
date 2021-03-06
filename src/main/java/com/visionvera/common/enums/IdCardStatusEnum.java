package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-09-24 09:09
 */
public enum IdCardStatusEnum {

    NORMAL(0, "设备正常工作"),
    ABNORMAL(1, "设备异常"),
    NODEVICE(2, "设备未连接"),
    NOCARD(3, "未扫描到身份证或请重新放置身份证");


    private Integer value;
    private String name;

    IdCardStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static IdCardStatusEnum valueToEnum(int value) {
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

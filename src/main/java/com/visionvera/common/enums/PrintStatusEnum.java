package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-09-24 09:09
 */
public enum PrintStatusEnum {

    NORMAL(0, "打印机正常工作"),
    STOP(1, "打印机暂不可用"),
    NODEVICE(2, "设备未连接或故障"),
    NOPAPER(3, "打印机无纸"),
    NOINK(4, "打印机无墨"),
    JAMMEDPAPER(5, "打印机卡纸"),
    COVEROPEN(6, "打印机前盖或后盖未关闭"),
    NOENOUGHSTORAGE(7, "打印机内存不足");


    private Integer value;
    private String name;

    PrintStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static PrintStatusEnum valueToEnum(int value) {
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

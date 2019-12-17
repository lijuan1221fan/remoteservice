package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-11-22 16:02
 */
public enum ScuOnlineStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(0, "离线");
    private Integer value;
    private String name;

    ScuOnlineStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ScuOnlineStatusEnum valueToEnum(int value) {
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

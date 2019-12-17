package com.visionvera.common.enums;

public enum ConfigTypeEnum {

    X86ConfigFile(1, "x86配置文件"),
    X86UpPackage(2, "x86升级包");

    private Integer value;
    private String name;

    ConfigTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ConfigTypeEnum valueToEnum(Integer value) {
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

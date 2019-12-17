package com.visionvera.common.enums;

public enum DevTypeEnum {

    AllInOne(1,"一体机"),
    CueingMachine(2,"叫号机");

    private Integer value;
    private String name;

    DevTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static DevTypeEnum valueToEnum(int value) {
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

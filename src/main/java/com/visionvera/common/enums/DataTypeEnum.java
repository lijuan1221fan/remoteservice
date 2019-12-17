package com.visionvera.common.enums;

public enum DataTypeEnum {

    String(1, "字符串"),
    Integer(2, "整数"),
    Float(3, "浮点数"),
    Boolean(4, "布尔类型");

    private Integer value;
    private String name;

    DataTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static DataTypeEnum valueToEnum(Integer value) {
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

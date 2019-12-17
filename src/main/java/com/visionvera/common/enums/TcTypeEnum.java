package com.visionvera.common.enums;

public enum TcTypeEnum {

    TC_ASSIST(1, "统筹中心（协助）"),
    TC_ACCEPT(2, "统筹中心（受理）");

    private Integer type;
    private String desc;

    TcTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static TcTypeEnum typeToEnum(int type) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getType() == type) {
                return values()[k];
            }
        }
        return null;
    }

    public static TcTypeEnum descToEnum(String desc) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getDesc().equals(desc)) {
                return values()[k];
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

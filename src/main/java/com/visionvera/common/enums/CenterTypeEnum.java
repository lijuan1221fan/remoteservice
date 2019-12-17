package com.visionvera.common.enums;

/**
 * Created by jlm on 2019-07-17 19:02
 */
public enum CenterTypeEnum {
    /**
     *
     */
    ADMIN(1L, "统筹中心"),
    /**
     *
     */
    CHECK(2L, "审核中心"),
    /**
     *
     */
    SERVER(3L, "服务中心");

    private Long type;
    private String desc;

    CenterTypeEnum(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static CenterTypeEnum typeToEnum(int type) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getType() == type) {
                return values()[k];
            }
        }
        return null;
    }

    public static CenterTypeEnum descToEnum(String desc) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getDesc().equals(desc)) {
                return values()[k];
            }
        }
        return null;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

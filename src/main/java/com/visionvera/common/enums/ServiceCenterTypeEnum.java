package com.visionvera.common.enums;

/**
 * 服务中心枚举类
 *
 * @author ericshen
 * @date 2018-11-29
 */
public enum ServiceCenterTypeEnum {

  /**
   *
   */
  ADMIN(1, "统筹中心"),
  /**
   *
   */
  CHECK(2, "审核中心"),
  /**
   *
   */
  SERVER(3, "服务中心");

  private Integer type;
  private String desc;

  ServiceCenterTypeEnum(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public static ServiceCenterTypeEnum typeToEnum(int type) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getType() == type) {
        return values()[k];
      }
    }
    return null;
  }

  public static ServiceCenterTypeEnum descToEnum(String desc) {
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
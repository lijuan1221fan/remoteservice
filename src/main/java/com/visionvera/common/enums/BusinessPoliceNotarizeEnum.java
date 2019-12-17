package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameBusinessPoliceNotarizeEnum 是否需要公安信息确认单 0：不需要 1：需要
 * @date 2018/12/20 version
 */
public enum BusinessPoliceNotarizeEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessPoliceNotarizeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessPoliceNotarizeEnum valueToEnum(int value) {
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

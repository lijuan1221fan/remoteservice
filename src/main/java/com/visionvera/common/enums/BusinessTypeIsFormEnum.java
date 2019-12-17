package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: BusinessTypeIsFormEnum
 * @Author: ljfan
 * @Date: 2019-06-10 17:40
 * @Version: V1.0
 */
public enum BusinessTypeIsFormEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessTypeIsFormEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessTypeIsFormEnum valueToEnum(int value) {
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

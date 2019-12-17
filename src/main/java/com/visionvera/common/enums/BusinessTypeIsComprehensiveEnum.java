package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: BusinessTypeIsComprehensiveEnum 是否仅支持统筹受理
 * @Author: ljfan
 * @Date: 2019-06-12 10:09
 * @Version: V1.0
 */

public enum BusinessTypeIsComprehensiveEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessTypeIsComprehensiveEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessTypeIsComprehensiveEnum valueToEnum(int value) {
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

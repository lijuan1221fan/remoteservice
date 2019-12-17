package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: AndroidShowTypeEnum
 * @Author: ljfan
 * @Date: 2019-04-25 16:16
 * @Version: V1.0
 */
public enum AndroidShowTypeEnum {
  OnlyTakeNumber("01", "仅叫号"),
  OnlyShowNumber("02", "仅提醒"),
  OnlyTakeAndShowNumber("03", "仅叫号提醒");
  private String value;
  private String name;

  AndroidShowTypeEnum(String value, String name) {
    this.value = value;
    this.name = name;
  }

  public static AndroidShowTypeEnum valueToEnum(String value) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getValue() == value) {
        return values()[k];
      }
    }
    return null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

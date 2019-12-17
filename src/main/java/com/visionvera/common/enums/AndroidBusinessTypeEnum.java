package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: AndroidBusinessTypeEnum
 * @Author: ljfan      分部门处理
 * @Date: 2019-04-25 15:57
 * @Version: V1.0
 */

public enum AndroidBusinessTypeEnum {
  AllProcess("01", "一窗综办"),
  DepartmentalProcess("02", "分部门办理");
  private String value;
  private String name;

  AndroidBusinessTypeEnum(String value, String name) {
    this.value = value;
    this.name = name;
  }

  public static AndroidBusinessTypeEnum valueToEnum(String value) {
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

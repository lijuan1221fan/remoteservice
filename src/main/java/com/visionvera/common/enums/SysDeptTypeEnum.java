package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: SysDeptTypeEnum
 * @Author: ljfan
 * @Date: 2019-07-09 16:38
 * @Version: V1.0
 */
public enum SysDeptTypeEnum {
  Yes(0,"是一窗宗办"),
  No(1,"不是一窗宗办");

  private Integer value;
  private String name;

  SysDeptTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static SysDeptTypeEnum valueToEnum(int value) {
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

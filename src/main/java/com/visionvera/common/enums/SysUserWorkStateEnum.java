package com.visionvera.common.enums;/**
 * 功能描述:
 *
 * @ClassName: SysUserWorkStateEnum
 * @Author: ljfan
 * @Date: 2019-05-10 09:49
 * @Version: V1.0
 * @param 0：空闲中 1：等待中 2：处理中
 */
public enum SysUserWorkStateEnum {
  Free(0, "空闲中"),
  Waiting(1, "等待中"),
  Processing(2,"处理中");
  private Integer value;
  private String name;

  SysUserWorkStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static SysUserWorkStateEnum valueToEnum(int value) {
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

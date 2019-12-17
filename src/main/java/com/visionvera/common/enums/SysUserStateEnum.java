package com.visionvera.common.enums;/**
 * 功能描述:
 *
 * @ClassName: SysUserStateEnum
 * @Author: ljfan
 * @Date: 2019-01-26 17:36
 * @Version: V1.0
 * 状态 1:在线 0:离线，有效
 */

public enum SysUserStateEnum {
  OnLine(1, "在线"),
  OffLine(0, "离线");
  private Integer value;
  private String name;

  SysUserStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static SysUserStateEnum valueToEnum(int value) {
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

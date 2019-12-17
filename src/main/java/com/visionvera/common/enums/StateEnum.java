package com.visionvera.common.enums;/**
 * 功能描述:
 *
 * @ClassName: StateEnum
 *description 数据状态 1:有效 0:无效 -1:禁用
 * @Author: ljfan
 * @Date: 2019-01-11 11:57
 * @Version: V1.0
 */
public enum StateEnum {
  Effective(1,"有效"),
  Invalid(0,"无效"),
  Disable(-1,"禁用");
  private Integer value;
  private String name;

  StateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static StateEnum valueToEnum(int value) {
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

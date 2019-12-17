package com.visionvera.common.enums;/**
 * 功能描述:
 *
 * @ClassName: FinalStateEnum
 * @Author: ljfan
 * @Date: 2019-02-25 13:56
 * @Version: V1.0
 */

public enum FinalStateEnum {
  Untreated(0,"待处理"),
  Processed(1,"已处理"),
  NotHandled(2,"未处理");
  private Integer value;
  private String name;

  FinalStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static FinalStateEnum valueToEnum(int value) {
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

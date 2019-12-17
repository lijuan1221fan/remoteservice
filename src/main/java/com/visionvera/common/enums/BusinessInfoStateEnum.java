package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: BusinessStateEnum
 * description 业务状态:1 未处理 2处理中 3已处理 4过号'
 * @Author: ljfan
 * @Date: 2019-01-11 12:09
 * @Version: V1.0
 */
public enum BusinessInfoStateEnum {
  Untreated(1,"待处理"),
  Processing(2,"处理中"),
  Processed(3,"已结束"),
  OverNumber(4,"过号");
  private Integer value;
  private String name;

  BusinessInfoStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessInfoStateEnum valueToEnum(int value) {
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

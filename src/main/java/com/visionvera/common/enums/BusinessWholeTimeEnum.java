package com.visionvera.common.enums;

/**
 * @ClassName BusinessWholeTimeEnum
 *description 是否全时段
 * @author ljfan
 * @date 2019/05/16
 *version
 */
public enum BusinessWholeTimeEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessWholeTimeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessWholeTimeEnum valueToEnum(int value) {
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

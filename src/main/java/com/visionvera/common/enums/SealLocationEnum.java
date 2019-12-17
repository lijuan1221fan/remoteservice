package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameSingLocalEnum description TODO
 * @date 2018/12/21 version
 */
public enum SealLocationEnum {
  LocationHeight(170, "height"),
  LocationWidth(170, "width"),
  LocationX(550, "x"),
  LocationY(650, "y");
  private Integer value;
  private String name;

  SealLocationEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static SealLocationEnum valueToEnum(int value) {
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

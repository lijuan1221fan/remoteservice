package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameBusinessMaterialsPrintEnum 是否需要本地打印申请材料 0：不需要 1：需要
 * @date 2018/12/20 version
 */
public enum BusinessMaterialsPrintEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessMaterialsPrintEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessMaterialsPrintEnum valueToEnum(int value) {
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
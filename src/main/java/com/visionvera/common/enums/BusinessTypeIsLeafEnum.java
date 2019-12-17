package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameBusinessTypeIsLeaf 是否是叶子节点  0:否 1:是
 * @date 2018/12/19 version
 */
public enum BusinessTypeIsLeafEnum {
  Yes(1, "是"),
  No(0, "否");
  private Integer value;
  private String name;

  BusinessTypeIsLeafEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessTypeIsLeafEnum valueToEnum(int value) {
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

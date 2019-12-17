package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNamephotoConfigTypeEnum 照片类型类型：1：签名，2:盖章
 * @date 2018/12/21 version
 */
public enum PhotoConfigTypeEnum {
  ESignature(1, "签名"),
  Seal(2, "盖章");
  private Integer value;
  private String name;

  PhotoConfigTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static PhotoConfigTypeEnum valueToEnum(int value) {
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

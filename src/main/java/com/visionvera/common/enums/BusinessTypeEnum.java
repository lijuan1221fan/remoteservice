package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameBusinessType 业务类型：0 ：普通   1：默认 ,综合业务 '
 * @date 2018/12/19 version
 */
public enum BusinessTypeEnum {
  Normal("0", "普通"),
  Default("1", "默认");
  private String value;
  private String name;

  BusinessTypeEnum(String value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessTypeEnum valueToEnum(String value) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getValue() == value) {
        return values()[k];
      }
    }
    return null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}

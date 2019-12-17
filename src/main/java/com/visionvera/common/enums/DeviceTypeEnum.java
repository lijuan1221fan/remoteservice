package com.visionvera.common.enums;

/**
 * 设备类型枚举
 *
 * @author ericshen
 * @date 2018-11-29
 */
public enum DeviceTypeEnum {
  /**
   * pad叫号机
   */
  PAD("01", "pad"),
  /**
   * 叫号机一体机
   */
  AIO("02", "一体机"),
  /**
   * 打印机
   */
  PRINTER("03", "打印机");

  private String type;
  private String name;

  public static final String[] AndroidDevice = {PAD.getType(), AIO.getType()};


  DeviceTypeEnum(String type, String name) {
    this.type = type;
    this.name = name;
  }

  public static DeviceTypeEnum typeToEnum(String type) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getType().equals(type)) {
        return values()[k];
      }
    }
    return null;
  }

  public static DeviceTypeEnum nameToEnum(String desc) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getName().equals(desc)) {
        return values()[k];
      }
    }
    return null;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

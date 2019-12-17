package com.visionvera.common.enums;

/**
 * 设备状态枚举类
 *
 * @author ericshen
 * @date 2018-11-29
 */
public enum DeviceStatusEnum {
  /**
   * 已删除
   */
  DELETED(-1, "删除"),
  /**
   * 正常可用
   */
  ENABLED(0, "正常"),
  /**
   * 已禁用
   */
  DISABLED(2, "不可用");

  private Integer value;
  private String desc;

  DeviceStatusEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public static DeviceStatusEnum valueToEnum(int value) {
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

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}

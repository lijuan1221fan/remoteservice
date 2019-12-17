package com.visionvera.common.enums;

/**
 * @author ljfan
 * @ClassNameShowTypeEnum 开启分享屏幕
 * @date 2018/12/17 version
 */
public enum ShowScreenEnum {
  //type 开启：1，关闭:2
  /**
   * pad叫号机
   */
  OPEN_THEN_SHARE_SCREEN(1, "开启分享屏幕"),
  /**
   * 叫号机一体机
   */
  CLOSE_OPEN_THEN_SHARE_SCREEN(2, "关闭分享屏幕");

  private Integer value;
  private String name;

  ShowScreenEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static ShowScreenEnum valueToEnum(Integer value) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getValue().equals(value)) {
        return values()[k];
      }
    }
    return null;
  }

  public static ShowScreenEnum nameToEnum(String name) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getName().equals(name)) {
        return values()[k];
      }
    }
    return null;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer type) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

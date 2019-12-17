package com.visionvera.common.enums;

/**
 * 功能描述: des type 视频类型，1正在录制的视频，默认值；2录制完毕的视频
 *
 * @ClassName: StorageTypeEnum
 * @Author: ljfan
 * @Date: 2019-01-23 16:59
 * @Version: V1.0
 */

public enum StorageTypeEnum {
  Processing(1, "处理中"),
  Processed(2, "已处理");
  private Integer value;
  private String name;

  StorageTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static StorageTypeEnum valueToEnum(int value) {
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

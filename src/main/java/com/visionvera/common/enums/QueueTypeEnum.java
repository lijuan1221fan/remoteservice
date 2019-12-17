package com.visionvera.common.enums;
/**
 * @ClassNameTypeEnum
 *description 取号场景 1:现场取号，2:预约取号
 * @author ljfan
 * @date 2019/03/27
 *version
 */
public enum QueueTypeEnum {
  Scene(1,"现场取号"),
  Booking(2,"预约取号");
  private Integer value;
  private String name;

  QueueTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static QueueTypeEnum valueToEnum(int value) {
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

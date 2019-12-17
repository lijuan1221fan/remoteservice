package com.visionvera.common.enums;
/**
 * @ClassNameTimeEnum
 *description 时间点枚举值
 * @author ljfan
 * @date 2019/03/22
 *version
 */
public enum ReservationTimeEnum {
  MorningStartTime(9,"9点"),
  MorningEndTime(12,"12点"),
  AfterStartTime(13,"13点"),
  AfterEndTime(17,"17点");
  private Integer value;
  private String name;
  ReservationTimeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static ReservationTimeEnum valueToEnum(Integer value) {
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

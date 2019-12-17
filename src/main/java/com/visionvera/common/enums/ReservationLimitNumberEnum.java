package com.visionvera.common.enums;
/**
 * @ClassNameReservationLimitNumber
 *description 业务预约设定最大预约值
 * @author ljfan
 * @date 2019/03/29
 *version
 */
public enum ReservationLimitNumberEnum {
  InitialVal(5,"默认值");
  private Integer value;
  private String name;

  ReservationLimitNumberEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static ReservationLimitNumberEnum valueToEnum(int value) {
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

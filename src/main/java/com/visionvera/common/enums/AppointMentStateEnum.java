package com.visionvera.common.enums;
/**
 * @ClassNameAppointMentStateEnum
 *description 预约状态:1 预约成功 2 签到成功 3 取消 4 过号
 * @author ljfan
 * @date 2019/03/25
 *version
 */
public enum AppointMentStateEnum {
  Have(4,"过号"),
  Cancle(3,"取消"),
  SignINg(2,"签到成功"),
  Effective(1,"有效"),
  Invalid(0,"无效"),
  Disable(-1,"禁用");
  private Integer value;
  private String name;

  AppointMentStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static AppointMentStateEnum valueToEnum(int value) {
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

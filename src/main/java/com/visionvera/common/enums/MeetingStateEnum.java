package com.visionvera.common.enums;

/**
 * 功能描述:
 *
 * @ClassName: MeetingStateEnum des 状态,0:删除,1:会议中,2:已停会
 * @Author: ljfan
 * @Date: 2019-01-23 16:28
 * @Version: V1.0
 */
public enum MeetingStateEnum {
  Stoped(2, "已停会"),
  Meeting(1, "会议中"),
  Del(0, "无效");
  private Integer value;
  private String name;

  MeetingStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static MeetingStateEnum valueToEnum(int value) {
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

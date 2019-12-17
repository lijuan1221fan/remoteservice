package com.visionvera.remoteservice.constant;

/**
 * @author ljfan
 * @ClassNameOperationFlow description 主要业务流程
 * @date 2018/11/14 version
 */
public enum OperationFlow {
  IDENTITY_AUTHTICATION("身份鉴权", 1), GREEN("材料收集", 2), BLANK("业务确认", 3);
  // 成员变量
  private String name;
  private Integer value;

  // 构造方法
  private OperationFlow(String name, Integer value) {
    this.name = name;
    this.value = value;
  }

  // 普通方法
  public static String getName(Integer value) {
    for (OperationFlow o : OperationFlow.values()) {
      if (o.getValue() == value) {
        return o.name;
      }
    }
    return null;
  }

  public static Integer getValue(Integer value) {
    for (OperationFlow o : OperationFlow.values()) {
      if (o.getValue() == value) {
        return o.getValue();
      }
    }
    return null;
  }

  // get set 方法
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

}

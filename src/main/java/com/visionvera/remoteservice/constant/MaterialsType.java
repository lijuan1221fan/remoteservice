package com.visionvera.remoteservice.constant;

/**
 * @author ljfan
 * @ClassNameMaterialsType 材料申请单
 * @date 2018/11/15 version
 */
public enum MaterialsType {
  //1:申请单 2：附加申请材料 3：申请材料 4：业务确认书',Application form
  IS_FORM("申请单", 1), APPEND_FORM("附加申请材料", 2), IS_MATERIALS("申请材料", 3), POLICE_NOTARIZE("业务确认书", 4);
  // 成员变量
  private String name;
  private Integer value;

  // 构造方法
  private MaterialsType(String name, Integer value) {
    this.name = name;
    this.value = value;
  }

  // 普通方法
  public static String getName(Integer value) {
    for (MaterialsType m : MaterialsType.values()) {
      if (m.getValue() == value) {
        return m.name;
      }
    }
    return null;
  }

  public static Integer getValue(Integer value) {
    for (MaterialsType o : MaterialsType.values()) {
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

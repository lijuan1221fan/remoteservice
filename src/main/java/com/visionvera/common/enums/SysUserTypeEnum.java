package com.visionvera.common.enums;
/**
 * @ClassNameSysUserTypeEnum
 *description 用户类型   0超管，1统筹中心管理员，2审核中心管理员，3业务员
 * @author ljfan
 * @date 2019/01/10
 *version
 */
public enum SysUserTypeEnum {
  Admin("0", "超管"),
  WholeCenterAdmin("1", "统筹中心管理员"),
  AuditCenterAdmin("2", "审核中心管理员"),
  WholeCenterSalesman("3","审批中心业务员"),
  AuditCenterSalesman("4","统筹中心业务员");

  private String value;
  private String name;

  SysUserTypeEnum(String value, String name) {
    this.value = value;
    this.name = name;
  }

  public static SysUserTypeEnum valueToEnum(String value) {
    for (int k = 0; k < values().length; k++) {
      if (values()[k].getValue() == value) {
        return values()[k];
      }
    }
    return null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

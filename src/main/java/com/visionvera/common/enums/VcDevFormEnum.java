package com.visionvera.common.enums;

/**
 * @author jlm
 * @ClassName: 终端形态  0：未分配 1：服务中心终端 2：审批中心终端 3：统筹终端
 * @Description:
 * @date 2018/12/24
 */
public enum VcDevFormEnum {
  Undistributed(0, "未分配"),
  Town(1, "服务中心终端"),
  Approval(2, "审批中心终端"),
  Whole(3, "统筹终端");

  private Integer value;
  private String name;

  VcDevFormEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static VcDevFormEnum valueToEnum(int value) {
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


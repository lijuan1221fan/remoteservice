package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: BusinessTypeSupReceiptEnum
 * @Author: ljfan
 * @Date: 2019-06-03 18:00
 * @info: 回执单模式：1:查看本地回执单，2单独上传电子签名模式，3无需提供业务回执
 * @Version: V1.0
 */

public enum BusinessTypeSupReceiptEnum {

  LocalReceipts(1,"查看本地回执单"),
  UploadSignAlone(2,"单独上传电子签名模式"),
  NotReceipt(3,"无需提供业务回执");
  private Integer value;
  private String name;

  BusinessTypeSupReceiptEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static BusinessTypeSupReceiptEnum valueToEnum(int value) {
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

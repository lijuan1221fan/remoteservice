package com.visionvera.common.enums;/**
 * 功能描述:
 *
 * @ClassName: UploadStateEnum
 * description 1:有效 0:无效 -1:禁用 2:生成签名图片 3:生成签章图片 4:生成图片加签章图片
 * @Author: ljfan
 * @Date: 2019-01-11 13:37
 * @Version: V1.0
 */
public enum UploadStateEnum {
  GenerateImageVeriSignPicture(4,"生成图片加签章图片"),
  VeriSignPicture(3,"生成签章图片"),
  SignaturPicture(2,"生成签名图片"),
  Effective(1,"有效"),
  Invalid(0,"无效"),
  Disable(-1,"禁用");
  private Integer value;
  private String name;

  UploadStateEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static UploadStateEnum valueToEnum(int value) {
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

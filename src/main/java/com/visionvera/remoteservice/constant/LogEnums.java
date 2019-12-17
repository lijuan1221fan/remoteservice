package com.visionvera.remoteservice.constant;


/**
 * @author lijintao
 */

public enum LogEnums {
  BUSINESS_PROCESS(1, "开始办理"),
  BUSINESS_IDENTIFICATION_CARD(2, "识别身份证"),
  BUSINESS_PRINTSCREEN(3, "视频截图"),
  BUSINESS_DELPRINTSCREEN(4, "删除截图"),
  BUSINESS_COMPLETED(5, "完成办理"),
  BUSINESS_SIGNED_PHOTO(6, "采集签名"),
  BUSINESS_DELETE_SIGNED_PHOTO(7, "删除签名"),
  BUSINESS_SCAN_FINGERPRINT(8, "扫描指纹照"),
  BUSINESS_DELETE_SCAN_FINGERPRINT(9, "删除指纹照"),
  BUSINESS_HIGH_SPEED_PHOTOGRAPHIC(10, "采集材料"),
  BUSINESS_DELETE_HIGH_SPEED_PHOTOGRAPHIC(11, "删除材料");
  private Integer code;
  private String value;

  LogEnums(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

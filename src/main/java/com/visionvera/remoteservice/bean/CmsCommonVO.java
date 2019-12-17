package com.visionvera.remoteservice.bean;

public class CmsCommonVO {

  /***
   * 返还结果
   */
  private boolean result;
  /***
   * 返回数据
   */
  private String data;
  /***
   * 结果描述
   */
  private String msg;

  public CmsCommonVO() {
    super();
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}

package com.visionvera.remoteservice.bean;

/**
 * @author ljfan
 * @ClassNameSendSmsBean description TODO
 * @date 2018/12/12 version
 */
public class SendSmsBean {

  //待办理数
  private Integer num;
  //手机号码
  private String mobilePhone;
  /**
   * 业务类型名称
   */
  private String businessTypeName;
  //真实姓名
  private String userName;

  public String getBusinessTypeName() {
    return businessTypeName;
  }

  public void setBusinessTypeName(String businessTypeName) {
    this.businessTypeName = businessTypeName;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}

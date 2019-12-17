package com.visionvera.remoteservice.bean;

/**
 * ClassName: BusinessDetailsClass
 *
 * @author quboka
 * @Description: 业务详情分类表
 * @date 2018年5月8日
 */
public class BusinessDetailsClass {

  private String number;//详情分类编码（详情类型应遵循业务类型表）
  private String typeDetail;//详细类型描述
  private String businessType;//业务类型（参照业务类型表）
  private String describe;//类型描述（参照业务类型表）
  private String type;//大的类型  1000：公安 2000：社保
  private Integer state;//状态 1有效 0无效，-1禁用

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getTypeDetail() {
    return typeDetail;
  }

  public void setTypeDetail(String typeDetail) {
    this.typeDetail = typeDetail;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "BusinessDetailsClassification [number=" + number
        + ", typeDetail=" + typeDetail + ", businessType="
        + businessType + ", describe=" + describe + ", type=" + type
        + ", state=" + state + "]";
  }

}

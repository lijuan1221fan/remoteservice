/*
 * NewbornBaseInfo.java
 * ------------------*
 * 2018-06-20 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;

/**
 * ----------------------* 2018-06-20created
 */
public class NewbornBaseInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  /**
   * 业务唯一key，生成号码时候对应生成的key
   */
  private String businessKey;
  /**
   * 迁入前地址
   */
  private String preAddress;
  /**
   * 迁入前派出所
   */
  private String prePoliceStation;
  /**
   * 迁入地址
   */
  private String address;
  /**
   * 迁入派出所
   */
  private String policeStation;
  /**
   * 申请事由
   */
  private String excuse;
  /**
   * 被投靠户户主意见
   */
  private String comment;
  /**
   * 现住址信息
   */
  private String addressInformation;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey == null ? null : businessKey.trim();
  }

  public String getPreAddress() {
    return preAddress;
  }

  public void setPreAddress(String preAddress) {
    this.preAddress = preAddress == null ? null : preAddress.trim();
  }

  public String getPrePoliceStation() {
    return prePoliceStation;
  }

  public void setPrePoliceStation(String prePoliceStation) {
    this.prePoliceStation = prePoliceStation == null ? null : prePoliceStation.trim();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address == null ? null : address.trim();
  }

  public String getPoliceStation() {
    return policeStation;
  }

  public void setPoliceStation(String policeStation) {
    this.policeStation = policeStation == null ? null : policeStation.trim();
  }

  public String getExcuse() {
    return excuse;
  }

  public void setExcuse(String excuse) {
    this.excuse = excuse == null ? null : excuse.trim();
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment == null ? null : comment.trim();
  }

  public String getAddressInformation() {
    return addressInformation;
  }

  public void setAddressInformation(String addressInformation) {
    this.addressInformation = addressInformation == null ? null : addressInformation.trim();
  }

}
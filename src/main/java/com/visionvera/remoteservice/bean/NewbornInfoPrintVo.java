/*
 * NewbornInfo.java
 * ------------------*
 * 2018-06-20 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 新生儿信息表 ----------------------* 2018-06-20created
 */
public class NewbornInfoPrintVo implements Serializable {

  private static final long serialVersionUID = -5779053426492866038L;
  private Integer id;
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
  /**
   * 申请人姓名
   */
  private String applicantName;
  /**
   * 申请人身份证号
   */
  private String IDNumber;
  /**
   * 申请人联系方式
   */
  private String phone;
  /**
   * 签名照
   */
  private String signedPhoto;
  /**
   * 受理单位
   */
  private String affiliation;
  /**
   * 操作员
   */
  private String userName;
  /**
   * 办理时间
   */
  private Date businessHandlingTime;
  /**
   * 业务类型
   */
  private String businessType;
  /**
   * 材料数量
   */
  private Integer materialNum;
  private List<NewbornInfo> newbornInfos;

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getBusinessHandlingTime() {
    return businessHandlingTime;
  }

  public void setBusinessHandlingTime(Date businessHandlingTime) {
    this.businessHandlingTime = businessHandlingTime;
  }

  public Integer getMaterialNum() {
    return materialNum;
  }

  public void setMaterialNum(Integer materialNum) {
    this.materialNum = materialNum;
  }

  public String getSignedPhoto() {
    return signedPhoto;
  }

  public void setSignedPhoto(String signedPhoto) {
    this.signedPhoto = signedPhoto;
  }

  public String getPreAddress() {
    return preAddress;
  }

  public void setPreAddress(String preAddress) {
    this.preAddress = preAddress;
  }

  public String getPrePoliceStation() {
    return prePoliceStation;
  }

  public void setPrePoliceStation(String prePoliceStation) {
    this.prePoliceStation = prePoliceStation;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPoliceStation() {
    return policeStation;
  }

  public void setPoliceStation(String policeStation) {
    this.policeStation = policeStation;
  }

  public String getExcuse() {
    return excuse;
  }

  public void setExcuse(String excuse) {
    this.excuse = excuse;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getAddressInformation() {
    return addressInformation;
  }

  public void setAddressInformation(String addressInformation) {
    this.addressInformation = addressInformation;
  }

  public String getApplicantName() {
    return applicantName;
  }

  public void setApplicantName(String applicantName) {
    this.applicantName = applicantName;
  }

  public String getIDNumber() {
    return IDNumber;
  }

  public void setIDNumber(String IDNumber) {
    this.IDNumber = IDNumber;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<NewbornInfo> getNewbornInfos() {
    return newbornInfos;
  }

  public void setNewbornInfos(List<NewbornInfo> newbornInfos) {
    this.newbornInfos = newbornInfos;
  }

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return "NewbornInfoPrintVo{" +
        "id=" + id +
        ", preAddress='" + preAddress + '\'' +
        ", prePoliceStation='" + prePoliceStation + '\'' +
        ", address='" + address + '\'' +
        ", policeStation='" + policeStation + '\'' +
        ", excuse='" + excuse + '\'' +
        ", comment='" + comment + '\'' +
        ", addressInformation='" + addressInformation + '\'' +
        ", applicantName='" + applicantName + '\'' +
        ", IDNumber='" + IDNumber + '\'' +
        ", phone='" + phone + '\'' +
        ", signedPhoto='" + signedPhoto + '\'' +
        ", affiliation='" + affiliation + '\'' +
        ", userName='" + userName + '\'' +
        ", businessHandlingTime=" + businessHandlingTime +
        ", businessType='" + businessType + '\'' +
        ", materialNum=" + materialNum +
        ", newbornInfos=" + newbornInfos +
        '}';
  }
}
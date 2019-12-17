package com.visionvera.remoteservice.bean;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * ClassName: ProjectChangeBean
 *
 * @author quboka
 * @Description: 项目变更
 * @date 2018年5月8日
 */
public class ProjectChangeBean {

  private Integer id;
  private String businessKey;//业务key
  private String applicantName;//申请人姓名
  private String IDNumber;//证件号码
  private String address;//地址
  private String phone;//联系方式
  private String cause;//变更原因
  private String projectName;//项目名称
  private String beforeChangeNumber;//变更前业务详情号码（参照t_business_details_classification  业务详情分类表）
  private String afterChangeNumber;//变更后业务详情号码（参照t_business_details_classification  业务详情分类表）
  private String userName;//用户真实姓名（受理人）
  @DateTimeFormat
  private Date changeTime;// 变更时间
  private String affiliation;//归属

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

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
    this.businessKey = businessKey;
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

  public void setIDNumber(String iDNumber) {
    IDNumber = iDNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCause() {
    return cause;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getBeforeChangeNumber() {
    return beforeChangeNumber;
  }

  public void setBeforeChangeNumber(String beforeChangeNumber) {
    this.beforeChangeNumber = beforeChangeNumber;
  }

  public String getAfterChangeNumber() {
    return afterChangeNumber;
  }

  public void setAfterChangeNumber(String afterChangeNumber) {
    this.afterChangeNumber = afterChangeNumber;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Date getChangeTime() {
    return changeTime;
  }

  public void setChangeTime(Date changeTime) {
    this.changeTime = changeTime;
  }

  @Override
  public String toString() {
    return "ProjectChangeBean [id=" + id + ", businessKey=" + businessKey
        + ", applicantName=" + applicantName + ", IDNumber=" + IDNumber
        + ", address=" + address + ", phone=" + phone + ", cause="
        + cause + ", projectName=" + projectName
        + ", beforeChangeNumber=" + beforeChangeNumber
        + ", afterChangeNumber=" + afterChangeNumber + ", userName="
        + userName + ", changeTime=" + changeTime + "]";
  }

}

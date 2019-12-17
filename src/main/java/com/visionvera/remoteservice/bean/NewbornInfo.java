/*
 * NewbornInfo.java
 * ------------------*
 * 2018-06-20 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;

/**
 * 新生儿信息表 ----------------------* 2018-06-20created
 */
public class NewbornInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  /**
   * 主表关联id
   */
  private Integer baseId;
  /**
   * 出生日期
   */
  private String birthDate;
  /**
   * 申请人与变动人关系
   */
  private String relation;
  /**
   * 联系方式
   */
  private String phone;
  /**
   * 新生儿姓名
   */
  private String fullName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getBaseId() {
    return baseId;
  }

  public void setBaseId(Integer baseId) {
    this.baseId = baseId;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getRelation() {
    return relation;
  }

  public void setRelation(String relation) {
    this.relation = relation == null ? null : relation.trim();
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone == null ? null : phone.trim();
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName == null ? null : fullName.trim();
  }
}
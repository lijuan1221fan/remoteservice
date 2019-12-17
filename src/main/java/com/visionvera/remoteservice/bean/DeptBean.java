package com.visionvera.remoteservice.bean;

import java.util.Date;

/**
 * 部门bean
 */
public class DeptBean {

  private Integer id;
  /**
   * 审批中心key
   */
  private String serviceKey;
  /**
   * 部门名称
   */
  private String deptName;
  /**
   * 部门标题
   */
  private String deptTitle;
  /**
   * 部门联系人
   */
  private String deptContacts;
  /**
   * 部门联系人电话
   */
  private String deptContactsPhone;
  /**
   * 部门状态  -2 删除  -1：禁用  0：启用
   */
  private Integer deptStatus;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改时间
   */
  private Date updateTime;
  /**
   * 叫号前缀
   */
  private String numberPrefix;
  /**
   * 审批中心名称
   */
  private String serviceName;

  /**
   * 等待人数
   */
  private Long waitCount;

  public Long getWaitCount() {
    return waitCount;
  }

  public void setWaitCount(Long waitCount) {
    this.waitCount = waitCount;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 审批中心key
   *
   * @return service_key 审批中心key
   */
  public String getServiceKey() {
    return serviceKey;
  }

  /**
   * 审批中心key
   *
   * @param serviceKey 审批中心key
   */
  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey == null ? null : serviceKey.trim();
  }

  /**
   * 部门名称
   *
   * @return dept_name 部门名称
   */
  public String getDeptName() {
    return deptName;
  }

  /**
   * 部门名称
   *
   * @param deptName 部门名称
   */
  public void setDeptName(String deptName) {
    this.deptName = deptName == null ? null : deptName.trim();
  }

  /**
   * 部门标题
   *
   * @return dept_title 部门标题
   */
  public String getDeptTitle() {
    return deptTitle;
  }

  /**
   * 部门标题
   *
   * @param deptTitle 部门标题
   */
  public void setDeptTitle(String deptTitle) {
    this.deptTitle = deptTitle == null ? null : deptTitle.trim();
  }

  /**
   * 部门联系人
   *
   * @return dept_contacts 部门联系人
   */
  public String getDeptContacts() {
    return deptContacts;
  }

  /**
   * 部门联系人
   *
   * @param deptContacts 部门联系人
   */
  public void setDeptContacts(String deptContacts) {
    this.deptContacts = deptContacts == null ? null : deptContacts.trim();
  }

  /**
   * 部门联系人电话
   *
   * @return dept_contacts_phone 部门联系人电话
   */
  public String getDeptContactsPhone() {
    return deptContactsPhone;
  }

  /**
   * 部门联系人电话
   *
   * @param deptContactsPhone 部门联系人电话
   */
  public void setDeptContactsPhone(String deptContactsPhone) {
    this.deptContactsPhone = deptContactsPhone == null ? null : deptContactsPhone.trim();
  }

  /**
   * 部门状态  -2 删除  -1：禁用  0：启用
   *
   * @return dept_status 部门状态  -2 删除  -1：禁用  0：启用
   */
  public Integer getDeptStatus() {
    return deptStatus;
  }

  /**
   * 部门状态  -2 删除  -1：禁用  0：启用
   *
   * @param deptStatus 部门状态  -2 删除  -1：禁用  0：启用
   */
  public void setDeptStatus(Integer deptStatus) {
    this.deptStatus = deptStatus;
  }

  /**
   * 版本号
   *
   * @return version 版本号
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * 版本号
   *
   * @param version 版本号
   */
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   * 创建时间
   *
   * @return create_time 创建时间
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * 创建时间
   *
   * @param createTime 创建时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 修改时间
   *
   * @return update_time 修改时间
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * 修改时间
   *
   * @param updateTime 修改时间
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getNumberPrefix() {
    return numberPrefix;
  }

  public void setNumberPrefix(String numberPrefix) {
    this.numberPrefix = numberPrefix == null ? null : numberPrefix.trim();
  }
}
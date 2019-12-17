package com.visionvera.remoteservice.bean;

public class NumberBean {

  /**
   * * 号码每天从0开始
   */
  private Integer number;
  /**
   * * 审批中心唯一key
   */
  private String serviceKey;

  /**
   * * 部门id
   */
  private Integer deptId;

  public NumberBean(String serviceKey, Integer deptId) {
    this.serviceKey = serviceKey;
    this.deptId = deptId;
  }

  public NumberBean() {
  }

  /**
   * 号码每天从0开始
   *
   * @return number 号码每天从0开始
   */
  public Integer getNumber() {
    return number;
  }

  /**
   * 号码每天从0开始
   *
   * @param number 号码每天从0开始
   */
  public void setNumber(Integer number) {
    this.number = number;
  }

  /**
   * 审批中心唯一key
   *
   * @return service_key 审批中心唯一key
   */
  public String getServiceKey() {
    return serviceKey;
  }

  /**
   * 审批中心唯一key
   *
   * @param serviceKey 审批中心唯一key
   */
  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey == null ? null : serviceKey.trim();
  }

  /**
   * 部门id
   *
   * @return dept_id 部门id
   */
  public Integer getDeptId() {
    return deptId;
  }

  /**
   * 部门id
   *
   * @param deptId 部门id
   */
  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }
}
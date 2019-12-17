/*
 * BusinessInfo.java
 * ------------------*
 * 2018-08-22 created
 */
package com.visionvera.app.entity;

import com.visionvera.app.group.ReservationGroup;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 ** 2019-03-18 created
 */
public class AppointmentInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  /**
   * app用户id
   */
  @NotNull(groups = {ReservationGroup.class},message = "预约用户id不能为空")
  private Long appUserId;
  /**
   * 身份证号码
   */
  @NotNull(groups = {ReservationGroup.class},message = "预约用户身份证号码不能为空")
  private String idCardNo;
  /**
   * 创建时间
   */
  private String createTime;
  /**
   * 更新时间
   */
  private String updateTime;
  /**
   * 开始时间
   */
 // @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @NotNull(groups = {ReservationGroup.class},message = "开始时间不能为空")
  private String startTime;
  /**
   * 结束时间
   */
 // @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @NotNull(groups = {ReservationGroup.class},message = "结束时间不能为空")
  private String endTime;
  /**
   * 预约状态:1 预约成功 2 签到成功 3 取消 4 过号
   */
  private Integer state;
  /**
   * 服务中心key
   */
  private String serviceKey;
  /**
   * 部门id
   */
  @NotNull(groups = {ReservationGroup.class},message = "部门ID不能为空")
  private Integer deptId;
  /**
   * 业务详情id
   */
  @NotNull(groups = {ReservationGroup.class},message = "业务id不能为空")
  private Integer businessType;
  /**
   * 业务详情名称
   */
  @NotNull(groups = {ReservationGroup.class},message = "业务名称不能为空")
  private String businessTypeName;

  /**
   * 描述
   */
  private String remarks;

  /**
   * 上午预约号
   */
  private Integer morningNumber;
  /**
   * 下午预约号
   */
  private Integer afternoonNumber;

  /**
   * 上午限制号数
   * @return
   */
  private Integer morningLimitNumber;
  /**
   * 下午限制号数
   */
  private Integer afterLimitNumber;

  /**
   * 预约日
   * @return
   */
  private String appointmentDay;

  public String getAppointmentDay() {
    return appointmentDay;
  }

  public void setAppointmentDay(String appointmentDay) {
    this.appointmentDay = appointmentDay;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Integer getMorningNumber() {
    return morningNumber;
  }

  public void setMorningNumber(Integer morningNumber) {
    this.morningNumber = morningNumber;
  }

  public Integer getAfternoonNumber() {
    return afternoonNumber;
  }

  public void setAfternoonNumber(Integer afternoonNumber) {
    this.afternoonNumber = afternoonNumber;
  }

  public AppointmentInfo() {

  }

  public AppointmentInfo( String serviceKey, Integer deptId, Integer businessType,
      String businessTypeName) {
    this.serviceKey = serviceKey;
    this.deptId = deptId;
    this.businessType = businessType;
    this.businessTypeName = businessTypeName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(Long appUserId) {
    this.appUserId = appUserId;
  }

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Integer getBusinessType() {
    return businessType;
  }

  public void setBusinessType(Integer businessType) {
    this.businessType = businessType;
  }

  public String getBusinessTypeName() {
    return businessTypeName;
  }

  public void setBusinessTypeName(String businessTypeName) {
    this.businessTypeName = businessTypeName;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Integer getMorningLimitNumber() {
    return morningLimitNumber;
  }

  public void setMorningLimitNumber(Integer morningLimitNumber) {
    this.morningLimitNumber = morningLimitNumber;
  }

  public Integer getAfterLimitNumber() {
    return afterLimitNumber;
  }

  public void setAfterLimitNumber(Integer afterLimitNumber) {
    this.afterLimitNumber = afterLimitNumber;
  }

}

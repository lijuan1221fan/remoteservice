/*
 * MeetingOperation.java
 * ------------------*
 * 2018-08-23 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ----------------------* 2018-08-23created
 *
 * @author lijintao
 */
public class MeetingOperation implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  /**
   * 业务唯一id
   */
  private Integer businessId;
  /**
   * 会议id
   */
  private String scheduleId;
  /**
   * 窗口id
   */
  private Integer userId;
  /**
   * 会议终端列表
   */
  private String terminalList;
  /**
   * 主席ip
   */
  private String ip;
  /**
   * 状态,0:会议中,1:已停会
   */
  private Integer state;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 修改时间
   */
  private Date updateTime;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 是否分享  isShare  0 ：未开启  1：已开启
   */
  private Integer isShare;
  /**
   * 动态入会得终端号码
   */
  private String dynDeviceNumber;
  /**
   * 开会使用的存储网关地址
   */
  private String storageAddress;

  /**
   * 会议名称
   */
  private String meetingName;

  public String getMeetingName() {
    return meetingName;
  }

  public void setMeetingName(String meetingName) {
    this.meetingName = meetingName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId == null ? null : scheduleId.trim();
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getTerminalList() {
    return terminalList;
  }

  public void setTerminalList(String terminalList) {
    this.terminalList = terminalList == null ? null : terminalList.trim();
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip == null ? null : ip.trim();
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Integer getIsShare() {
    return isShare;
  }

  public void setIsShare(Integer isShare) {
    this.isShare = isShare;
  }

  public String getDynDeviceNumber() {
    return dynDeviceNumber;
  }

  public void setDynDeviceNumber(String dynDeviceNumber) {
    this.dynDeviceNumber = dynDeviceNumber;
  }

  public String getStorageAddress() {
    return storageAddress;
  }

  public void setStorageAddress(String storageAddress) {
    this.storageAddress = storageAddress;
  }
  
}

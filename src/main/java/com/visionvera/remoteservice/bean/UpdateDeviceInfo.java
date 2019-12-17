/*
 * DeviceInfo.java
 * ------------------*
 * 2018-08-22 created
 */
package com.visionvera.remoteservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.validation.constraints.*;
/**
 * ----------------------* 2018-08-22created
 */
public class UpdateDeviceInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * 设备ID
   */
  private Integer id;
  /**
   * 所属服务中心
   */
  @NotEmpty(message = "服务中心key不能为空")
  private String serviceKey;
  /**
   * 设备私有名称
   */
  private String privateName;
  /**
   * 设备物理名称
   */
  private String deviceName;
  /**
   * 类型  1:pad 2:一体机 3:打印机
   */
  private String type;
  /**
   * 状态  0：可用 -1:删除  2：不可用
   */
  private Integer state;
  /**
   * ip
   */
  private String ip;
  /**
   * 端口
   */
  private Integer port;
  /**
   * 详细地址
   */
  private String address;
  /**
   * 备注
   */
  private String remark;
  /**
   * 设备密码（叫号机）
   */
  @NotEmpty(message = "密码不能为空")
  @JsonIgnore
  private String password;
  /**
   * 设备唯一标识（叫号机）
   */
  @NotEmpty(message = "设备标识不能为空")
  private String deviceCode;
  /**
   * 设备所在ip（叫号机）
   */
  @NotEmpty(message = "设备ip不能为空")
  private String deviceIp;
  /**
   * 打印机类型 常用打印机1 申请单打印机2
   */
  private String printerType;
  /**
   * 服务中心名称
   */
  private String serviceName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey == null ? null : serviceKey.trim();
  }

  public String getPrivateName() {
    return privateName;
  }

  public void setPrivateName(String privateName) {
    this.privateName = privateName == null ? null : privateName.trim();
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName == null ? null : deviceName.trim();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type == null ? null : type.trim();
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip == null ? null : ip.trim();
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address == null ? null : address.trim();
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark == null ? null : remark.trim();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password == null ? null : password.trim();
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public void setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode == null ? null : deviceCode.trim();
  }

  public String getDeviceIp() {
    return deviceIp;
  }

  public void setDeviceIp(String deviceIp) {
    this.deviceIp = deviceIp == null ? null : deviceIp.trim();
  }

  public String getPrinterType() {
    return printerType;
  }

  public void setPrinterType(String printerType) {
    this.printerType = printerType == null ? null : printerType.trim();
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}

package com.visionvera.remoteservice.bean;

import java.io.Serializable;

/**
 * ClassName: Terminal
 *
 * @author quboka
 * @Description: 终端表
 * @date 2018年3月22日
 */
public class TerminalBean implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = -4709828771092435489L;

  private Integer terminalId;// 终端id
  private String terminalName;// 终端名称
  private String number;// 终端号
  private String ip;//终端ip
  private String serviceKey;// 服务中心key
  private Integer type;// 终端机的种类 1：启明 2：极光 3：手机 4.安卓盒子
  private Integer status;// 状态 0：空闲 1：使用中 -1:不可用 -2：删除
  private String address;// 详细地址
  private String createTime;// 创建时间
  private String updateTime;// 更新时间
  private Integer version;// 版本号
  private String serviceName;// 服务中心名称

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getTerminalName() {
    return terminalName;
  }

  public void setTerminalName(String terminalName) {
    this.terminalName = terminalName;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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

  public Integer getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(Integer terminalId) {
    this.terminalId = terminalId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "TerminalBean [terminalId=" + terminalId + ", terminalName="
        + terminalName + ", number=" + number + ", ip=" + ip
        + ", serviceKey=" + serviceKey + ", type=" + type + ", status="
        + status + ", address=" + address + ", createTime="
        + createTime + ", updateTime=" + updateTime + ", version="
        + version + "]";
  }

}

package com.visionvera.remoteservice.bean;

/**
 * 会议使用终端vo
 *
 * @author wrz
 */
public class DeviceVO {

  /**
   * 终端号码
   */
  private String id;
  /***
   * 终端名称
   */
  private String name;
  /**
   * 终端角色
   */
  private String roleId;
  /**
   * 终端类型
   */
  private String typeId;
  /**
   * 终端IP
   */
  private String ip;
  /***
   * 主消息号
   */
  private String msgId;
  /***
   * 监控号
   */
  private String monitorId;
  /***
   * 通道号
   */
  private String channelId;
  /**
   * 大小流，1大流，0小流，默认大流
   */
  private String streamType;

  public DeviceVO() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getMonitorId() {
    return monitorId;
  }

  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getStreamType() {
    return streamType;
  }

  public void setStreamType(String streamType) {
    this.streamType = streamType;
  }

  @Override
  public String toString() {
    return "DeviceVO [id=" + id + ", name=" + name + ", roleId=" + roleId
        + ", typeId=" + typeId + ", ip=" + ip + ", msgId=" + msgId
        + ", monitorId=" + monitorId + ", channelId=" + channelId
        + ", streamType=" + streamType + "]";
  }

}

package com.visionvera.remoteservice.bean;

/**
 * @author hakimjun
 * @Description: TODO
 * @date 2019/9/3
 */
public class EmbededServerBackUpBean {

  //主键
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  //终端号
  private String vcDevId;

  public String getVcDevId() {
    return vcDevId;
  }

  public void setVcDevId(String vcDevId) {
    this.vcDevId = vcDevId;
  }

  public Integer getScuState() {
    return scuState;
  }

  public void setScuState(Integer scuState) {
    this.scuState = scuState;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * x86设备状态
   */
  private Integer scuState;//scu状态
  private String version;
}

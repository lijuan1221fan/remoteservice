package com.visionvera.remoteservice.bean;

/**
 * @author hakimjun
 * @Description: TODO
 * @date 2019/8/28
 */
public class versionUpdateBean {

  /**
   * 版本号
   */
  public String version;
  public void setVersion(String version) {
    this.version = version;
  }
  public String getVersion() {
    return version;
  }

  /**
   * 种类
   */
  public Integer type;
  public void setType(Integer type) {
    this.type = type;
  }
  public Integer getType() {
    return type;
  }

}

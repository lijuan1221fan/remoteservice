package com.visionvera.remoteservice.bean;

import java.util.Date;

/**
 * ClassName: CommonConfigBean
 *
 * @author quboka
 * @Description: 通用设置表
 * @date 2018年3月19日
 */
public class CommonConfigBean {

  private Integer id;

  /**
   * 通用名称
   */
  private String commonName;
  /**
   * 通用值
   */
  private String commonValue;
  /**
   * 状态
   */
  private Integer state;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 修改时间
   */
  private Date modifyTime;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 描述
   */
  private String describes;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCommonName() {
    return commonName;
  }

  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  public String getCommonValue() {
    return commonValue;
  }

  public void setCommonValue(String commonValue) {
    this.commonValue = commonValue;
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

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getDescribes() {
    return describes;
  }

  public void setDescribes(String describes) {
    this.describes = describes;
  }
}

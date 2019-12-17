package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lijintao
 */
public class Forms implements Serializable {

  private static final long serialVersionUID = -4186711162543316636L;
  /**
   * * 主键id
   */
  private Integer id;

  /**
   * * 名称
   */
  private String name;

  /**
   * * 表单地址
   */
  private String formaddress;

  /**
   * * 状态 0：正常  -1：删除
   */
  private Integer state;

  /**
   * * 版本号
   */
  private Integer version;

  /**
   * * 创建时间
   */
  private Date createtime;

  /**
   * * 修改时间
   */
  private Date modifytime;
  /**
   * * 修改时间
   */
  private String businessType;

  /**
   * 主键id
   *
   * @return id 主键id
   */
  public Integer getId() {
    return id;
  }

  /**
   * 主键id
   *
   * @param id 主键id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 名称
   *
   * @return name 名称
   */
  public String getName() {
    return name;
  }

  /**
   * 名称
   *
   * @param name 名称
   */
  public void setName(String name) {
    this.name = name == null ? null : name.trim();
  }

  /**
   * 表单地址
   *
   * @return formAddress 表单地址
   */
  public String getFormaddress() {
    return formaddress;
  }

  /**
   * 表单地址
   *
   * @param formaddress 表单地址
   */
  public void setFormaddress(String formaddress) {
    this.formaddress = formaddress == null ? null : formaddress.trim();
  }

  /**
   * 状态 0：正常  -1：删除
   *
   * @return state 状态 0：正常  -1：删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 状态 0：正常  -1：删除
   *
   * @param state 状态 0：正常  -1：删除
   */
  public void setState(Integer state) {
    this.state = state;
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
   * @return createTime 创建时间
   */
  public Date getCreatetime() {
    return createtime;
  }

  /**
   * 创建时间
   *
   * @param createtime 创建时间
   */
  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  /**
   * 修改时间
   *
   * @return modifyTime 修改时间
   */
  public Date getModifytime() {
    return modifytime;
  }

  /**
   * 修改时间
   *
   * @param modifytime 修改时间
   */
  public void setModifytime(Date modifytime) {
    this.modifytime = modifytime;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }
}
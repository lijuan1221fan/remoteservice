/*
 * PhotoBean.java
 * ------------------*
 * 2018-08-27 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ----------------------* 2018-08-27created
 */
public class PhotoBean implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * 图片主键
   */
  private Integer id;
  /**
   * 图片类型  图片类型id 参照t_picture_type表
   */
  private Integer pictureType;
  /**
   * 证件类型id 。参照t_materials表
   */
  private Integer materialsId;
  /**
   * 证件类型id 。参照t_materials表
   */
  private String materialsName;
  /**
   * 会议id
   */
  private String guid;
  /**
   * 业务唯一id
   */
  private Integer businessId;
  /**
   * 终端号码
   */
  private String v2vId;
  /**
   * 图片的id
   */
  private Integer pictureId;
  /**
   * 大图的地址
   */
  private String framePath;
  /**
   * 缩略图地址
   */
  private String iconPath;
  /**
   * 状态 1:有效 0:无效 -1:禁用
   */
  private Integer state;
  /**
   * timestamp
   */
  private Date createTime;
  /**
   * 更新时间
   */
  private Date updateTime;
  /**
   * 图片高度
   */
  private Integer heigth;
  /**
   * 图片宽
   */
  private Integer width;
  /**
   * 图片格式
   */
  private String format;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 图片类型名称
   */
  private String typeName;

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getPictureType() {
    return pictureType;
  }

  public void setPictureType(Integer pictureType) {
    this.pictureType = pictureType;
  }

  public Integer getMaterialsId() {
    return materialsId;
  }

  public void setMaterialsId(Integer materialsId) {
    this.materialsId = materialsId;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid == null ? null : guid.trim();
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public String getV2vId() {
    return v2vId;
  }

  public void setV2vId(String v2vId) {
    this.v2vId = v2vId == null ? null : v2vId.trim();
  }

  public Integer getPictureId() {
    return pictureId;
  }

  public void setPictureId(Integer pictureId) {
    this.pictureId = pictureId;
  }

  public String getFramePath() {
    return framePath;
  }

  public void setFramePath(String framePath) {
    this.framePath = framePath == null ? null : framePath.trim();
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath == null ? null : iconPath.trim();
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getHeigth() {
    return heigth;
  }

  public void setHeigth(Integer heigth) {
    this.heigth = heigth;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format == null ? null : format.trim();
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getMaterialsName() {
    return materialsName;
  }

  public void setMaterialsName(String materialsName) {
    this.materialsName = materialsName;
  }

  @Override
  public String toString() {
    return "PhotoBean{" +
            "id=" + id +
            ", pictureType=" + pictureType +
            ", materialsId=" + materialsId +
            ", materialsName='" + materialsName + '\'' +
            ", guid='" + guid + '\'' +
            ", businessId=" + businessId +
            ", v2vId='" + v2vId + '\'' +
            ", pictureId=" + pictureId +
            ", framePath='" + framePath + '\'' +
            ", iconPath='" + iconPath + '\'' +
            ", state=" + state +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", heigth=" + heigth +
            ", width=" + width +
            ", format='" + format + '\'' +
            ", version=" + version +
            ", typeName='" + typeName + '\'' +
            '}';
  }
}

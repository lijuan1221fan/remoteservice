/*
 * BusinessLog.java
 * ------------------*
 * 2018-08-23 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * ----------------------* 2018-08-23created
 *
 * @author lijintao
 */
public class BusinessLog implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * 主键
   */
  private Integer id;
  /**
   * 业务表id
   */
  private Integer businessId;
  /**
   * 业务日志类型 ,参照业务日志类型表
   */
  private Integer logType;
  /**
   * 业务日志创建时间
   */
  private Date createTime;
  /**
   * 业务日志描述
   */
  private String remark;
  /**
   * 图片id
   */
  private Integer photoId;
  /**
   * 大图的地址
   */
  private String framePath;
  /**
   * 缩略图地址
   */
  private String iconPath;

  public BusinessLog(Integer businessId, Integer logType, String remark, Integer photoId) {
    this.businessId = businessId;
    this.logType = logType;
    this.remark = remark;
    this.photoId = photoId;
  }

  public BusinessLog() {
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

  public Integer getLogType() {
    return logType;
  }

  public void setLogType(Integer logType) {
    this.logType = logType;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark == null ? null : remark.trim();
  }

  public Integer getPhotoId() {
    return photoId;
  }

  public void setPhotoId(Integer photoId) {
    this.photoId = photoId;
  }

  public String getFramePath() {
    return parsePath("frame_path", this.framePath);
  }

  public void setFramePath(String framePath) {
    this.framePath = framePath;
  }

  public String getIconPath() {
    return this.iconPath; //2018/11/23 parsePath("icon_path", this.iconPath);
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }

  private String parsePath(String field, String path) {
    if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(path)) {
      return "storage/" + this.getId() + "/" + field + "/img.jpg";
    }
    return "";
  }
}
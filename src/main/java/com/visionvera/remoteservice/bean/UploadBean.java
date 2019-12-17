package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ----------------------* 2018-08-29created
 */
public class UploadBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  /**
   * 唯一名称
   */
  private String uniqueName;
  /**
   * 文件描述
   */
  private String fileDescription;
  /**
   * 文件路径
   */
  private String filePath;
  /**
   * 原名称
   */
  private String originalFilename;
  /**
   * 文件类型(后缀)
   */
  private String fileType;
  /**
   * 用户id
   */
  private Integer userId;
  /**
   * 业务id
   */
  private Integer businessId;
  /**
   * 状态 1:有效 0:无效 -1:禁用
   */
  private Integer state;
  /**
   * 版本号
   */
  private Integer version;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改时间
   */
  private Date modifyTime;
  private String absolutePath;


  public UploadBean(String uniqueName, String fileDescription, String filePath,
      String originalFilename, String fileType, Integer userId, Integer businessId,
      String absolutePath) {
    this.uniqueName = uniqueName;
    this.fileDescription = fileDescription;
    this.filePath = filePath;
    this.originalFilename = originalFilename;
    this.fileType = fileType;
    this.userId = userId;
    this.businessId = businessId;
    this.absolutePath = absolutePath;
  }

  public UploadBean() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUniqueName() {
    return uniqueName;
  }

  public void setUniqueName(String uniqueName) {
    this.uniqueName = uniqueName == null ? null : uniqueName.trim();
  }

  public String getFileDescription() {
    return fileDescription;
  }

  public void setFileDescription(String fileDescription) {
    this.fileDescription = fileDescription == null ? null : fileDescription.trim();
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath == null ? null : filePath.trim();
  }

  public String getOriginalFilename() {
    return originalFilename;
  }

  public void setOriginalFilename(String originalFilename) {
    this.originalFilename = originalFilename == null ? null : originalFilename.trim();
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType == null ? null : fileType.trim();
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
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

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public String getAbsolutePath() {
    return absolutePath;
  }

  public void setAbsolutePath(String absolutePath) {
    this.absolutePath = absolutePath == null ? null : absolutePath.trim();
  }
}
